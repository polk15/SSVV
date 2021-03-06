package ssvv.example;

import Domain.Student;
import Domain.TemaLab;
import Exceptions.ServiceException;
import Exceptions.ValidatorException;
import Repository.XMLFileRepository.NotaXMLRepo;
import Repository.XMLFileRepository.StudentXMLRepo;
import Repository.XMLFileRepository.TemaLabXMLRepo;
import Service.TxtFileService.NotaService;
import Service.XMLFileService.NotaXMLService;
import Service.XMLFileService.StudentXMLService;
import Service.XMLFileService.TemaLabXMLService;
import Validator.NotaValidator;
import Validator.StudentValidator;
import Validator.TemaLabValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;


public class AppTest {
    private StudentXMLService studentXMLService;
    private TemaLabXMLService temaLabXMLService;
    private NotaXMLService notaXMLService;
    private TemaLabXMLRepo temaLabXMLRepo;
    private final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    @Before
    public void setup() {
        StudentValidator studentValidator = new StudentValidator();
        TemaLabValidator temaLabValidator = new TemaLabValidator();
        NotaValidator notaValidator = new NotaValidator();
        StudentXMLRepo studentXMLRepo = new StudentXMLRepo(studentValidator, "student-test.xml");
        NotaXMLRepo notaXMLRepo = new NotaXMLRepo(notaValidator, "nota-test.xml");
        temaLabXMLRepo = new TemaLabXMLRepo(temaLabValidator, "tema-lab-test.xml");
        studentXMLService = new StudentXMLService(studentXMLRepo);
        temaLabXMLService = new TemaLabXMLService(temaLabXMLRepo);
        notaXMLService = new NotaXMLService(notaXMLRepo);
    }

    @Test
    public void tc1_int_top_down() throws ValidatorException {
        // Integration testing - add Student + add Assignment

        tc1_int_wbt(); // Add student test case

        String idTema = "1", description = "descr1", saptLimita = "1", saptPredarii = "2";

        String[] paramsTema = {idTema, description, saptLimita, saptPredarii};
        temaLabXMLService.add(paramsTema);

        assertNotNull(temaLabXMLService.findOne(Integer.parseInt(idTema)));
    }

    @Test
    public void tc2_int_top_down() throws ValidatorException {
        // Integration testing - add Student + add Assignment + add Grade

        tc1_int_top_down(); // add Student + add Assignment integration test

        String idNota= "1", value = "10", data = "2020-10-10T10:10:10";
        String idStudent = "1";
        String idTema = "1";

        String[] paramsNota = {idNota, idStudent, idTema, value, data};
        notaXMLService.add(paramsNota);

        assertNotNull(notaXMLService.findOne(Integer.parseInt(idNota))); // Add Grade test
    }

    @Test
    public void tc_int_big_bang() throws ValidatorException {
        // Integration testing - big bang approach

        String idStudent = "1", nume = "name1", grupa = "934", email = "email1@gmail.com", tutor = "tutor1";
        String idTema = "1", description = "descr1", saptLimita = "1", saptPredarii = "2";
        String idNota= "1", value = "10", data = "2020-10-10T10:10:10";

        String[] paramsStudent = {idStudent, nume, grupa, email, tutor};
        String[] paramsTema = {idTema, description, saptLimita, saptPredarii};
        String[] paramsNota = {idNota, idStudent, idTema, value, data};

        studentXMLService.add(paramsStudent);
        temaLabXMLService.add(paramsTema);
        notaXMLService.add(paramsNota);

        assertNotNull(studentXMLService.findOne(idStudent));
        assertNotNull(temaLabXMLService.findOne(Integer.parseInt(idTema)));
        assertNotNull(notaXMLService.findOne(Integer.parseInt(idNota)));
    }

    @Test
    public void tc1_int_wbt() throws ValidatorException {
        // Add student
        String id = "1", nume = "name1", grupa = "934", email = "email1@gmail.com", tutor = "tutor1";
        String[] params = {id, nume, grupa, email, tutor};
        studentXMLService.add(params);
        assertNotNull(studentXMLService.findOne(id));
    }

    @Test
    public void tc2_int_wbt() throws ValidatorException {
        // Add assignment
        String id = "1", description = "descr1", saptLimita = "1", saptPredarii = "2";
        String[] params = {id, description, saptLimita, saptPredarii};
        temaLabXMLService.add(params);
        assertNotNull(temaLabXMLService.findOne(Integer.parseInt(id)));
    }

    @Test
    public void tc3_int_wbt() throws ValidatorException {
        // Add grade
        String id = "1", idStudent = "1", idTema = "1", value = "10", data = "2020-10-10T10:10:10";
        String[] params = {id, idStudent, idTema, value, data};
        notaXMLService.add(params);
        assertNotNull(notaXMLService.findOne(Integer.parseInt(id)));
    }

    @Test
    public void tc1_wbt() throws ValidatorException {
        String id = "1", description = "descr1", saptLimita = "1", saptPredarii = "2";
        String[] params = {id, description, saptLimita, saptPredarii};
        temaLabXMLService.add(params);
        assertNotNull(temaLabXMLService.findOne(Integer.parseInt(id)));
    }

    @Test
    public void tc2_wbt() throws ValidatorException {
        String sameId = "1";
        temaLabXMLService.add(new String[]{sameId, "descr1", "1", "2"});
        assertNotNull(temaLabXMLService.findOne(Integer.parseInt(sameId)));
        assertThrows(ServiceException.class, () -> temaLabXMLService.add(new String[]{sameId, "descr2", "2", "3"}));
    }

    @Test
    public void tc3_wbt() throws ValidatorException {
        TemaLab temaLab = new TemaLab(1,  "desc", 1, 2);
        temaLabXMLRepo.save(temaLab);
        assertNotNull(temaLabXMLService.findOne(1));
    }

    @Test(expected = ValidatorException.class)
    public void tc4_wbt() throws ValidatorException {
        TemaLab temaLab = new TemaLab(null,  "desc", 1, 2);
        temaLabXMLRepo.save(temaLab);
    }

    @Test(expected = ValidatorException.class)
    public void tc6_wbt() throws ValidatorException {
        TemaLab temaLab = new TemaLab(1,  null, 1, 2);
        temaLabXMLRepo.save(temaLab);
    }

    @Test(expected = ValidatorException.class)
    public void tc7_wbt() throws ValidatorException {
        TemaLab temaLab = new TemaLab(1,  "", 1, 2);
        temaLabXMLRepo.save(temaLab);
    }

    @Test(expected = ValidatorException.class)
    public void tc8_wbt() throws ValidatorException {
        TemaLab temaLab = new TemaLab(1,  "desc", 0, 2);
        temaLabXMLRepo.save(temaLab);
    }

    @Test(expected = ValidatorException.class)
    public void tc9_wbt() throws ValidatorException {
        TemaLab temaLab = new TemaLab(1,  "desc", 15, 2);
        temaLabXMLRepo.save(temaLab);
    }

    @Test(expected = ValidatorException.class)
    public void tc10_wbt() throws ValidatorException {
        TemaLab temaLab = new TemaLab(1,  "desc", 1, 0);
        temaLabXMLRepo.save(temaLab);
    }

    @Test(expected = ValidatorException.class)
    public void tc11_wbt() throws ValidatorException {
        TemaLab temaLab = new TemaLab(1,  "desc", 1, 15);
        temaLabXMLRepo.save(temaLab);
    }

    @Test
    public void tc1_bbt() throws ValidatorException {
        String id = "1", nume = "name1", grupa = "934", email = "email1@gmail.com", tutor = "tutor1";
        String[] params = {id, nume, grupa, email, tutor};
        studentXMLService.add(params);
        Student student1 = studentXMLService.findOne(id);

        int new_grupa = student1.getGrupa();
        assertEquals(new_grupa, Integer.parseInt(grupa)); // EC1

        assertTrue(new_grupa >= 100 && new_grupa <= 999); // EC3

        Matcher matcher = Pattern.compile(EMAIL_REGEX).matcher(student1.getEmail());
        assertTrue(matcher.matches()); // EC6

        assertNotEquals(student1.getId(), ""); // EC8

        assertNotEquals(student1.getNume(), ""); // EC10

        assertNotEquals(student1.getIndrumator(), ""); // EC12
    }

    @Test
    public void tc2_bbt() {
        String[] params = new String[]{"1", "name1", "string", "email1@gmail.com", "tutor1"};
        assertThrows(ValidatorException.class, () -> studentXMLService.add(params));
    }

    @Test
    public void tc3_bbt() {
        String[] params = new String[]{"1", "name1", "99", "email1@gmail.com", "tutor1"};
        assertThrows(ValidatorException.class, () -> studentXMLService.add(params));
    }

    @Test
    public void tc4_bbt() {
        String[] params = new String[]{"1", "name1", "1000", "email1@gmail.com", "tutor1"};
        assertThrows(ValidatorException.class, () -> studentXMLService.add(params));
    }

    @Test
    public void tc5_bbt() {
        String[] params = new String[]{"1", "name1", "934", "@.", "tutor1"};
        assertThrows(ValidatorException.class, () -> studentXMLService.add(params));
    }

    @Test
    public void tc6_bbt() {
        String[] params = new String[]{"", "name1", "934", "email1@gmail.com", "tutor1"};
        assertThrows(ValidatorException.class, () -> studentXMLService.add(params));
    }

    @Test
    public void tc7_bbt() {
        String[] params = new String[]{"1", "", "string", "email1@gmail.com", "tutor1"};
        assertThrows(ValidatorException.class, () -> studentXMLService.add(params));
    }

    @Test
    public void tc8_bbt() {
        String[] params = new String[]{"1", "name1", "string", "email1@gmail.com", ""};
        assertThrows(ValidatorException.class, () -> studentXMLService.add(params));
    }

    @Test
    public void tc9_bbt() throws ValidatorException {
        String[] params1 = new String[]{"1", "name1", "934", "email1@gmail.com", "tutor1"};
        String[] params2 = new String[]{"2", "name1", "934", "email1@gmail.com", "tutor1"};
        studentXMLService.add(params1);
        studentXMLService.add(params2);
        Student student1 = studentXMLService.findOne("1");
        Student student2 = studentXMLService.findOne("2");

        assertEquals(student1.getId(), "1");
        assertEquals(student2.getId(), "2");
    }

    @Test
    public void tc10_bbt() throws ValidatorException {
        String[] params1 = new String[]{"1", "name1", "934", "email1@gmail.com", "tutor1"};
        String[] params2 = new String[]{"1", "name1", "934", "email1@gmail.com", "tutor1"};
        studentXMLService.add(params1);
        assertThrows(Exception.class, () -> studentXMLService.add(params2));
    }

    @Test
    public void tc11_bbt() throws ValidatorException {
        String[] params = new String[]{"1", "name1", "100", "email1@gmail.com", ""};
        studentXMLService.add(params);
        Student student = studentXMLService.findOne("1");
        assertEquals(student.getGrupa(), Integer.parseInt("100")); // EC1
    }

    @Test
    public void tc12_bbt() throws ValidatorException {
        String[] params = new String[]{"1", "name1", "101", "email1@gmail.com", ""};
        studentXMLService.add(params);
        Student student = studentXMLService.findOne("1");
        assertEquals(student.getGrupa(), Integer.parseInt("101")); // EC1
    }

    @Test
    public void tc13_bbt() throws ValidatorException {
        String[] params = new String[]{"1", "name1", "998", "email1@gmail.com", ""};
        studentXMLService.add(params);
        Student student = studentXMLService.findOne("1");
        assertEquals(student.getGrupa(), Integer.parseInt("998")); // EC1
    }

    @Test
    public void tc14_bbt() throws ValidatorException {
        String[] params = new String[]{"1", "name1", "999", "email1@gmail.com", ""};
        studentXMLService.add(params);
        Student student = studentXMLService.findOne("1");
        assertEquals(student.getGrupa(), Integer.parseInt("999")); // EC1
    }
}
