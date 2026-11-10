// Express Router configuration for categories
const express = require('express');
const router = express.Router();
const categoryController = require('../controllers/category.controller');
const validateCategoryInput = require('../middlewares/validateCategory.middleware');

router.get('/', categoryController.getAllCategories);
router.post('/', validateCategoryInput, categoryController.createCategory);
router.get('/:id', categoryController.getCategoryById);
router.put('/:id', validateCategoryInput, categoryController.updateCategory);
router.patch('/:id', validateCategoryInput, categoryController.patchCategory);
router.delete('/:id', categoryController.deleteCategory);

module.exports = router;
