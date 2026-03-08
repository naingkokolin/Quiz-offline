import express from "express";
import cors from "cors";
import initDb from "./helpers/db.js";
import questionRoutes from "./routes/questionRoutes.js";
import historyRoutes from "./routes/historyRoutes.js";
import settings from "./routes/settings.js";

const app = express();
app.use(cors());
app.use(express.json());

initDb();
const port = 3000;

app.get('/', (req, res) => {
    res.send("hello world");
});

app.use("/api/questions", questionRoutes)
app.use("/api/history", historyRoutes);
app.use('/api/settings', settings);

app.listen(port, () => console.log("Server is running on port: 3000"));