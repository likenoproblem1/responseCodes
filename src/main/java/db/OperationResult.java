package db;

import java.util.Optional;

    public class OperationResult {

    private final boolean isSuccess;
    private final boolean isDuplicate;
    private final Optional<Throwable> errorOpt;

    public OperationResult(boolean isSuccess, boolean isDuplicate, Optional<Throwable> errorOpt) {
        this.isSuccess = isSuccess;
        this.isDuplicate = isDuplicate;
        this.errorOpt = errorOpt;
    }

    public static OperationResult failed(Throwable error) {
        return new OperationResult(false, false, Optional.of(error));
    }

    public static OperationResult success() {
        return new OperationResult(true, false, Optional.empty());
    }

    public static OperationResult duplicateEntry() {
        return new OperationResult(false, true, Optional.empty());
    }

    public boolean hasDuplicateEntry () {
        return isDuplicate;
    }

    public boolean isSuccessful() {
        return isSuccess;
    }

    public boolean isFailed() {
        return errorOpt.isPresent();
    }

    public Optional<Throwable> getError() {
        return errorOpt;
    }
}