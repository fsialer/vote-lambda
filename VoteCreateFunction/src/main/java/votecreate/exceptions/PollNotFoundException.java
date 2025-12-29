package votecreate.exceptions;

public class PollNotFoundException extends RuntimeException{
    public PollNotFoundException(String msg){
        super(msg);
    }
}
