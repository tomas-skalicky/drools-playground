package cz.skalicky.drools.playground.applicant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.Optional;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RuleTest {

    private KnowledgeBase knowledgeBase;

    @BeforeMethod
    public void registerRule() {

        KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        knowledgeBuilder.add(
                ResourceFactory.newClassPathResource("isApplicantsAgeSmallerThan18.drl", RuleTest.class),
                ResourceType.DRL);
        knowledgeBuilder.add(
                ResourceFactory.newClassPathResource("isApplicantsAgeGreaterOrEquals18.drl", RuleTest.class),
                ResourceType.DRL);

        if (knowledgeBuilder.hasErrors()) {
            throw new RuntimeException(Arrays.toString(knowledgeBuilder.getErrors().toArray()));
        }

        knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
        knowledgeBase.addKnowledgePackages(knowledgeBuilder.getKnowledgePackages());
    }

    @Test
    public void whenApplicatedIs13ThenInvalid() {

        testAgeRule(13, false);
    }

    @Test
    public void whenApplicatedIs18ThenValid() {

        testAgeRule(18, true);
    }
    
    private void testAgeRule(final int age, final boolean expectedValid) {


        Optional<StatefulKnowledgeSession> knowledgeSession = Optional.empty();
        KnowledgeRuntimeLogger logger = null;
        try {
            
            knowledgeSession = Optional.of(knowledgeBase.newStatefulKnowledgeSession());
            logger = KnowledgeRuntimeLoggerFactory.newFileLogger(knowledgeSession.get(), "testRule");

            Applicant applicant = new Applicant();
            applicant.setAge(age);
            knowledgeSession.get().insert(applicant);
            knowledgeSession.get().fireAllRules();
            
            for (Object o : knowledgeSession.get().getObjects()) {
                if (o instanceof Applicant) {
                    assertThat(((Applicant) o).isValid(), is(expectedValid));
                }
            }
            
        } finally {
            
            if (logger != null) {
                logger.close();
            }
            
            if (knowledgeSession.isPresent()) {
                knowledgeSession.get().dispose();
            }
        }
    }

}
