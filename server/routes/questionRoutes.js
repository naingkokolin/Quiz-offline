import express from "express";
import { createQuestion, getRandomQuestion, markQuestionAsUsed, deleteAllQuestions, uploadBulkQuestions, getAllQuestions, getQuestionsByCategory } from "../controllers/questionController.js";

const router = express.Router();

router.get("/", getAllQuestions);

router.post("/", createQuestion);

router.post("/bulk", uploadBulkQuestions);

router.get("/random/:category", getRandomQuestion);

router.patch("/:id/used", markQuestionAsUsed);

router.get("/category/:category", getQuestionsByCategory);

router.delete("/all", deleteAllQuestions);

export default router;