package com.josh.dog_adopting.adoption;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
class Assistant {

    @Bean
    ApplicationRunner runner(ChatClient chatClient) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                String content = chatClient.prompt()
                        .user("do you have any neurotic dogs?")
                        .call()
                        .content();
                System.out.println("reply [" + content + "]");
            }
        };
    }

    // prompt stuffing
    // RAG
    @Bean
    ChatClient chatClient(ChatClient.Builder builder,
                          DogRepository repository,
                          VectorStore vectorStore // cosine similarity
                          ) {

        if (false) {
            repository.findAll().forEach(dog -> {
                var document = new Document("id: %s, name %s, description: %s".formatted(dog.id(), dog.name(), dog.description()));
                vectorStore.add(List.of(document));
            });
        }

        var qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.5d)
                        .topK(6)
                        .build())
                .build();

        var system = """
			You are an AI powered assistant to help people adopt a dog
			from the adoption agency named Pooch Palace with locations in
			Seoul, Las Vegas, Tokyo, Krakow, Singapore, Paris, London, and
			San Francisco. If you don't know about the dogs housed at our particular
			stores, then return a disappointed response suggesting we don't
			have any dogs available.
			""";
        return builder
                .defaultSystem(system)
                .defaultAdvisors(qaAdvisor)
                .build();
    }
}
