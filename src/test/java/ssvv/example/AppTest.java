package ssvv.example;

import Domain.Student;
import Exceptions.ValidatorException;
import Repository.XMLFileRepository.StudentXMLRepo;
import Service.TxtFileService.StudentService;
import Service.XMLFileService.StudentXMLService;
import Validator.StudentValidator;
import org.junit.Test;

import static org.junit.Assert.*;


public class AppTest {
    private StudentXMLService studentXMLService;

    @Test
    public void TC1() throws ValidatorException {
        StudentValidator studentValidator = new StudentValidator();
        StudentXMLRepo studentXMLRepo = new StudentXMLRepo(studentValidator, "StudentiXML.xml");
        studentXMLService = new StudentXMLService(studentXMLRepo);

        String id = "1", nume = "name1", grupa = "1", email = "email1@gmail.com", prof = "prof1";
        String[] params = {id, nume, grupa, email, prof};
        studentXMLService.add(params);
        Student student1 = studentXMLService.findOne(id);
        assertEquals(student1.getNume(), nume);
        assertEquals(student1.getGrupa(), Integer.parseInt(grupa));
        assertEquals(student1.getEmail(), email);
        assertEquals(student1.getIndrumator(), prof);
    }

    @Test
    public void TCE1() {
        StudentValidator studentValidator = new StudentValidator();
        StudentXMLRepo studentXMLRepo = new StudentXMLRepo(studentValidator, "StudentiXML.xml");
        studentXMLService = new StudentXMLService(studentXMLRepo);

        String id = "1", nume = "name1", grupa = "invalid", email = "email1@gmail.com", prof = "prof1";
        String[] params = {id, nume, grupa, email, prof};
        ValidatorException e = assertThrows(ValidatorException.class, () -> studentXMLService.add(params));
    }
}
