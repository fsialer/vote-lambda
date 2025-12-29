package votecreate.exceptions;

public class LimitTimeVoteException extends RuntimeException{
    public LimitTimeVoteException(String message) {
        super(message);
    }
}
