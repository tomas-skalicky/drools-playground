package cz.skalicky.drools.playground.applicant;

import java.util.Arrays;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

public class Main {

    public static void main(String[] args) {

        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(ResourceFactory.newClassPathResource("isApplicantsAgeSmallerThan18.drl", Main.class),
                ResourceType.DRL);

        if (knowledgeBuilder.hasErrors()) {
            throw new RuntimeException(Arrays.toString(knowledgeBuilder.getErrors().toArray()));
        }

        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());

        System.out.println("success");
    }

}
