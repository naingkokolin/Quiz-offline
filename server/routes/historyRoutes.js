import express from "express";
import { saveQuizResult, getHistory } from "../controllers/historyController.js";

const router = express.Router();

router.post("/", saveQuizResult);
router.get("/", getHistory);

export default router;