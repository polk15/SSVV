package Service.XMLFileService;

import Domain.Student;
import Exceptions.ValidatorException;
import Repository.XMLFileRepository.StudentXMLRepo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentXMLService extends AbstractXMLService<String,Student>{
    private StudentXMLRepo xmlrepo;

    public StudentXMLService(StudentXMLRepo xmlrepo)  {
        super(xmlrepo);
    }

    @Override
    protected Student extractEntity(String[] params) throws ValidatorException {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

        int grupa=0;
        try{
            grupa=Integer.parseInt(params[2]);
            if (grupa < 100 || grupa > 999)
                throw new ValidatorException("Invalid group!");
            Matcher matcher = Pattern.compile(EMAIL_REGEX).matcher(params[3]);
            if (!matcher.matches())
                throw new ValidatorException("Invalid email!");
        }catch(NumberFormatException ex){
            System.out.println(ex.getMessage());
        }
        return new Student(params[0],params[1],grupa,params[3],params[4]);
    }

}