// Input validation for the Category endpoints.
// Runs before the controller: invalid input never reaches MongoDB.
function validateCategoryInput(req, res, next) {
    const isPatch = req.method === 'PATCH';
    const { name } = req.body || {};

    if (name === undefined) {
        if (isPatch) {
            return next(); // PATCH may update only the description
        }
        return res.status(400).json({
            success: false,
            error: 'Category name is required'
        });
    }

    if (typeof name !== 'string' || name.trim().length < 3) {
        return res.status(400).json({
            success: false,
            error: 'Category name must be at least 3 characters long'
        });
    }

    next();
}

module.exports = validateCategoryInput;
