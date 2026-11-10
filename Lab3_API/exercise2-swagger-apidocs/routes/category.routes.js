// Express Router configuration for categories
const express = require('express');
const router = express.Router();
const categoryController = require('../controllers/category.controller');
const validateCategoryInput = require('../middlewares/validateCategory.middleware');

/**
 * @swagger
 * /categories:
 *   get:
 *     summary: Get the list of categories
 *     description: Supports pagination and sorting.
 *     tags: [Categories]
 *     parameters:
 *       - in: query
 *         name: page
 *         schema:
 *           type: integer
 *           default: 1
 *         description: Current page (default = 1)
 *       - in: query
 *         name: limit
 *         schema:
 *           type: integer
 *           default: 5
 *         description: Items per page (default = 5)
 *       - in: query
 *         name: sort
 *         schema:
 *           type: string
 *           enum: [name, slug, createdAt, updatedAt]
 *           default: createdAt
 *         description: Field used for sorting
 *       - in: query
 *         name: order
 *         schema:
 *           type: string
 *           enum: [asc, desc]
 *           default: desc
 *         description: Sort direction
 *     responses:
 *       200:
 *         description: Categories retrieved successfully
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 count:
 *                   type: integer
 *                 pagination:
 *                   type: object
 *                   properties:
 *                     currentPage:
 *                       type: integer
 *                     totalPages:
 *                       type: integer
 *                     totalCategories:
 *                       type: integer
 *                     sort:
 *                       type: string
 *                 data:
 *                   type: array
 *                   items:
 *                     $ref: '#/components/schemas/Category'
 *   post:
 *     summary: Create a new category
 *     description: The slug is generated automatically from the name.
 *     tags: [Categories]
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/CategoryInput'
 *     responses:
 *       201:
 *         description: Category created successfully
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 message:
 *                   type: string
 *                 data:
 *                   $ref: '#/components/schemas/Category'
 *       400:
 *         description: Name missing or shorter than 3 characters
 *       409:
 *         description: Duplicate name or slug
 * /categories/{id}:
 *   get:
 *     summary: Get category details by ID
 *     tags: [Categories]
 *     parameters:
 *       - $ref: '#/components/parameters/Id'
 *     responses:
 *       200:
 *         description: Category found
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 data:
 *                   $ref: '#/components/schemas/Category'
 *       400:
 *         description: Malformed category ID
 *       404:
 *         description: Category not found
 *   put:
 *     summary: Update a category by ID
 *     description: Replaces name and description; the slug is regenerated when the name changes.
 *     tags: [Categories]
 *     parameters:
 *       - $ref: '#/components/parameters/Id'
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/CategoryInput'
 *     responses:
 *       200:
 *         description: Category updated successfully
 *         content:
 *           application/json:
 *             schema:
 *               type: object
 *               properties:
 *                 success:
 *                   type: boolean
 *                 message:
 *                   type: string
 *                 data:
 *                   $ref: '#/components/schemas/Category'
 *       400:
 *         description: Invalid ID or validation failed
 *       404:
 *         description: Category not found
 *       409:
 *         description: Duplicate name or slug
 *   patch:
 *     summary: Partially update a category by ID
 *     description: Send only the fields that need to change, e.g. the description.
 *     tags: [Categories]
 *     parameters:
 *       - $ref: '#/components/parameters/Id'
 *     requestBody:
 *       required: true
 *       content:
 *         application/json:
 *           schema:
 *             $ref: '#/components/schemas/CategoryInput'
 *     responses:
 *       200:
 *         description: Category updated successfully
 *       400:
 *         description: Invalid ID or invalid name
 *       404:
 *         description: Category not found
 *       409:
 *         description: Duplicate name or slug
 *   delete:
 *     summary: Delete a category by ID
 *     tags: [Categories]
 *     parameters:
 *       - $ref: '#/components/parameters/Id'
 *     responses:
 *       200:
 *         description: Category deleted successfully
 *       400:
 *         description: Malformed category ID
 *       404:
 *         description: Category not found
 */
router.get('/', categoryController.getAllCategories);
router.post('/', validateCategoryInput, categoryController.createCategory);
router.get('/:id', categoryController.getCategoryById);
router.put('/:id', validateCategoryInput, categoryController.updateCategory);
router.patch('/:id', validateCategoryInput, categoryController.patchCategory);
router.delete('/:id', categoryController.deleteCategory);

module.exports = router;
