data <- read.csv(file="D:/IdeaProjects/NeuralNet/src/main/resources/result.txt", header = TRUE, sep = ",", quote = "\"",dec = ".", fill = TRUE, comment.char = "")
data["div"] <- NA
data$div <- abs(data$Ist-data$Soll)
data["detected"] <- NA
data$detected <- ifelse(data$div < 0.5, 1 , 0)
table(data$Soll,data$detected)