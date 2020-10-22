postscript("PROBLEMS-0-0.TIME.Boxplot.eps", horizontal=FALSE, onefile=FALSE, height=8, width=12, pointsize=10)
resultDirectory<-"../data/"
qIndicator <- function(indicator, problem)
{
fileMoDagameNSGAII<-paste(resultDirectory, "MoDagameNSGAII", sep="/")
fileMoDagameNSGAII<-paste(fileMoDagameNSGAII, problem, sep="/")
fileMoDagameNSGAII<-paste(fileMoDagameNSGAII, indicator, sep="/")
MoDagameNSGAII<-scan(fileMoDagameNSGAII)

fileMoDagameIBEA<-paste(resultDirectory, "MoDagameIBEA", sep="/")
fileMoDagameIBEA<-paste(fileMoDagameIBEA, problem, sep="/")
fileMoDagameIBEA<-paste(fileMoDagameIBEA, indicator, sep="/")
MoDagameIBEA<-scan(fileMoDagameIBEA)

fileMoDagameMOCHC<-paste(resultDirectory, "MoDagameMOCHC", sep="/")
fileMoDagameMOCHC<-paste(fileMoDagameMOCHC, problem, sep="/")
fileMoDagameMOCHC<-paste(fileMoDagameMOCHC, indicator, sep="/")
MoDagameMOCHC<-scan(fileMoDagameMOCHC)

fileMoDagameMOCell<-paste(resultDirectory, "MoDagameMOCell", sep="/")
fileMoDagameMOCell<-paste(fileMoDagameMOCell, problem, sep="/")
fileMoDagameMOCell<-paste(fileMoDagameMOCell, indicator, sep="/")
MoDagameMOCell<-scan(fileMoDagameMOCell)

fileMoDagamePAES<-paste(resultDirectory, "MoDagamePAES", sep="/")
fileMoDagamePAES<-paste(fileMoDagamePAES, problem, sep="/")
fileMoDagamePAES<-paste(fileMoDagamePAES, indicator, sep="/")
MoDagamePAES<-scan(fileMoDagamePAES)

fileMoDagameSPEA2<-paste(resultDirectory, "MoDagameSPEA2", sep="/")
fileMoDagameSPEA2<-paste(fileMoDagameSPEA2, problem, sep="/")
fileMoDagameSPEA2<-paste(fileMoDagameSPEA2, indicator, sep="/")
MoDagameSPEA2<-scan(fileMoDagameSPEA2)

algs<-c("MoDagameNSGAII","MoDagameIBEA","MoDagameMOCHC","MoDagameMOCell","MoDagamePAES","MoDagameSPEA2")
boxplot(MoDagameNSGAII,MoDagameIBEA,MoDagameMOCHC,MoDagameMOCell,MoDagamePAES,MoDagameSPEA2,names=algs, notch = FALSE)
titulo <-paste(indicator, problem, sep=":")
title(main=titulo)
}
par(mfrow=c(1,1))
indicator<-"TIME"
qIndicator(indicator, "BerkeleyDBMemory")
