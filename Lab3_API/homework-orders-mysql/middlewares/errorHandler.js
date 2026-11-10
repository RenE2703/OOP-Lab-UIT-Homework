// 404 handler + global error handler
function notFound(req, res) {
    res.status(404).json({
        success: false,
        error: `Route ${req.method} ${req.originalUrl} not found`
    });
}

// eslint-disable-next-line no-unused-vars
function errorHandler(err, req, res, next) {
    let status = err.status || 500;
    let message = err.message || 'Internal server error';

    // Body parser (express.json) syntax error
    if (err instanceof SyntaxError && err.status === 400 && 'body' in err) {
        status = 400;
        message = 'Invalid JSON body';
    }

    if (err.code === 'ER_DUP_ENTRY') {
        status = 409;
        message = 'Duplicate value violates a unique constraint';
    }

    if (err.code === 'ER_NO_REFERENCED_ROW_2' || err.code === 'ER_ROW_IS_REFERENCED_2') {
        status = 400;
        message = 'The request violates a foreign-key constraint';
    }

    if (status >= 500) {
        console.error('Server Error:', err);
    }

    res.status(status).json({ success: false, error: message });
}

module.exports = { notFound, errorHandler };
