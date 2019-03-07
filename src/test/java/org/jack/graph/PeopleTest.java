package org.jack.graph;

import org.jack.graph.model.Person;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class PeopleTest {

    private PeopleService peopleService;

    @Before
    public void setup() throws IOException {
        peopleService = PeopleService.builder()
                .personCSVFilename("people.csv")
                .relationsCSVFilename("relationships.csv")
                .build();
    }

    @Test
    public void testPeopleBuilder()  {

        Collection<Person> people = peopleService.findAll();

        Assert.assertEquals(12, people.size());
    }

    /**
     <li>Bob (4 relationships)</li>
     <li>Jenny (3 relationships)</li>
     <li>Nigel (2 relationships)</li>
     <li>Alan (0 relationships)</li> **/
    @Test
    public void testCorrectNumberOfRelationsForPeople() {

        Person bob = peopleService.getPerson("bob@bob.com");
        Person jenny = peopleService.getPerson("jenny@toys.com");
        Person nigel = peopleService.getPerson("nigel@marketing.com");
        Person alan  = peopleService.getPerson("alan@lonely.org");

        Set<Person> bobsRelations = peopleService.getAdjacent(bob);
        Set<Person> jennysRelations = peopleService.getAdjacent(jenny);
        Set<Person> nigelRelations = peopleService.getAdjacent(nigel);
        Set<Person> alanRelations = peopleService.getAdjacent(alan);

        Assert.assertEquals(4, bobsRelations.size());
        Assert.assertEquals(3, jennysRelations.size());
        Assert.assertEquals(2, nigelRelations.size());
        Assert.assertEquals(0, alanRelations.size());
    }

    @Test
    public void testSizeOfFamilialRelations() {

        Person bob = peopleService.getPerson("bob@bob.com");
        Person jenny = peopleService.getPerson("jenny@toys.com");

        int sizeOfBobsFamilialRelations = peopleService.countAllFamilialRelations( bob );
        int sizeOfJennysFamilialRelations = peopleService.countAllFamilialRelations( jenny );


        Assert.assertEquals(4, sizeOfJennysFamilialRelations);
        Assert.assertEquals(4, sizeOfBobsFamilialRelations);
    }
}
