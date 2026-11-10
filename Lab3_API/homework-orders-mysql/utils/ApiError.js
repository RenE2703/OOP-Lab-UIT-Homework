// Error type that carries the HTTP status code to the error middleware
class ApiError extends Error {
    constructor(status, message) {
        super(message);
        this.status = status;
        Error.captureStackTrace(this, this.constructor);
    }
}

module.exports = ApiError;
