package org.jack.graph;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.jack.graph.model.Person;
import org.jack.graph.model.RelationType;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

@Slf4j
public class PeopleService {

    private final String personCSVFilename;

    private final String relationsCSVFilename;

    private MutableValueGraph<Person, RelationType> peopleGraph = ValueGraphBuilder.undirected().build();

    @Builder
    public PeopleService(String personCSVFilename, String relationsCSVFilename) throws IOException {
        this.personCSVFilename = personCSVFilename;
        this.relationsCSVFilename = relationsCSVFilename;

        populatePeople();
        populateRelationships();
    }

    private void populateRelationships() throws IOException {

        Reader reader = getReader( relationsCSVFilename );

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

        for (CSVRecord record : records) {
            Person person = getPerson(record.get(0));
            RelationType type = RelationType.valueOf(record.get(1));
            Person related = getPerson(record.get(2));

            peopleGraph.putEdgeValue(person, related, type);
        }
    }

    private void populatePeople() throws IOException {

        Reader reader = getReader( personCSVFilename );

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

        for (CSVRecord record : records) {
            String name = record.get(0);
            String email = record.get(1);
            Integer age = Integer.parseInt( record.get(2) );

            Person person = new Person(name, email, age);

            peopleGraph.addNode( person );
        }
    }

    private Reader getReader(String filename) {

        InputStream inputStream = PeopleService.class.getClassLoader().getResourceAsStream( filename );

        Reader reader = new InputStreamReader(inputStream);

        return reader;
    }

    public Person getPerson(Person person) {
        return peopleGraph.nodes().stream().filter(n -> n.equals(person)).findAny().get();
    }

    public Set<Person> getAdjacent(Person person) {
        return peopleGraph.adjacentNodes( person );
    }

    public Person getPerson(String email) {

        Person p = new Person(email);

        return getPerson(p);
    }

    public int countAllFamilialRelations(Person person) {

        Set<Person> relations = new HashSet<>();

        relations.add( person );

        countRelations(person, relations);

        return relations.size();
    }

    private void countRelations(Person person, Set<Person> relations) {

        Set<Person> adjacentPersons = getAdjacent( person );

        for (Person adjacentPerson : adjacentPersons) {

            RelationType type = peopleGraph.edgeValue(adjacentPerson, person).get();

            if(!type.equals(RelationType.FAMILY)) continue;

            if(!relations.contains( adjacentPerson )) {
                relations.add(adjacentPerson);
                countRelations(adjacentPerson, relations);
            }
        }
    }

    public Collection<Person> findAll() {

        return peopleGraph.nodes();
    }
}
