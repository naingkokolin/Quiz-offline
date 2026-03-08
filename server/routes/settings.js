import express from "express";
import multer from "multer";
import path from "path";
import { getSettings, updateSettings } from "../controllers/settings.js";

const router = express.Router();

const storage = multer.diskStorage({
  destination: "./uploads/",
  filename: (req, file, cb) => {
    cb(null, "current-logo" + path.extname(file.originalname));
  }
});
const upload = multer({ storage });

router.get("/", getSettings);
router.post("/update", upload.single("logo"), updateSettings);

export default router;