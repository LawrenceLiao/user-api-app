package co.zip.candidate.userapi.exception;

public class InsufficientMonthlySurplusException extends RuntimeException {
    public InsufficientMonthlySurplusException(String msg) {
        super(msg);
    }
}
