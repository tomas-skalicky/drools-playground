package cz.skalicky.drools.playground;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

public class Main {

	public static void main(String[] args) {

		KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		knowledgeBuilder.add(ResourceFactory.newClassPathResource(
				"isApplicantsAgeValid.drl", Main.class), ResourceType.DRL);

		if (knowledgeBuilder.hasErrors()) {
			System.err.println(knowledgeBuilder.getErrors().toString());
		}

		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(knowledgeBuilder
				.getKnowledgePackages());

		System.out.println("success");
	}

}
