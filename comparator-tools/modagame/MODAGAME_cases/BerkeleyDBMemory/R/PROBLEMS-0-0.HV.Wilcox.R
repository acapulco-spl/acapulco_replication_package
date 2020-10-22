write("", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex",append=FALSE)
resultDirectory<-"/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/data"
latexHeader <- function() {
  write("\\documentclass{article}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\title{StandardStudy}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\usepackage{amssymb}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\author{A.J.Nebro}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\begin{document}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\maketitle", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\section{Tables}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
}

latexTableHeader <- function(problem, tabularString, latexTableFirstLine) {
  write("\\begin{table}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\caption{", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write(problem, "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write(".HV.}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)

  write("\\label{Table:", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write(problem, "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write(".HV.}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)

  write("\\centering", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\begin{scriptsize}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\begin{tabular}{", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write(tabularString, "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write(latexTableFirstLine, "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\hline ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
}

latexTableTail <- function() { 
  write("\\hline", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\end{tabular}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\end{scriptsize}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  write("\\end{table}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
}

latexTail <- function() { 
  write("\\end{document}", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
}

printTableLine <- function(indicator, algorithm1, algorithm2, i, j, problem) { 
  file1<-paste(resultDirectory, algorithm1, sep="/")
  file1<-paste(file1, problem, sep="/")
  file1<-paste(file1, indicator, sep="/")
  data1<-scan(file1)
  file2<-paste(resultDirectory, algorithm2, sep="/")
  file2<-paste(file2, problem, sep="/")
  file2<-paste(file2, indicator, sep="/")
  data2<-scan(file2)
  if (i == j) {
    write("--", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  }
  else if (i < j) {
    if (wilcox.test(data1, data2)$p.value <= 0.05) {
      if (median(data1) >= median(data2)) {
        write("$\\blacktriangle$", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
      }
      else {
        write("$\\triangledown$", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE) 
      }
    }
    else {
      write("--", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE) 
    }
  }
  else {
    write(" ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
  }
}

### START OF SCRIPT 
# Constants
problemList <-c("BerkeleyDBMemory") 
algorithmList <-c("MoDagameNSGAII", "MoDagameIBEA", "MoDagameMOCHC", "MoDagameMOCell", "MoDagamePAES", "MoDagameSPEA2") 
tabularString <-c("lccccc") 
latexTableFirstLine <-c("\\hline  & MoDagameIBEA & MoDagameMOCHC & MoDagameMOCell & MoDagamePAES & MoDagameSPEA2\\\\ ") 
indicator<-"HV"

 # Step 1.  Writes the latex header
latexHeader()
# Step 2. Problem loop 
for (problem in problemList) {
  latexTableHeader(problem,  tabularString, latexTableFirstLine)

  indx = 0
  for (i in algorithmList) {
    if (i != "MoDagameSPEA2") {
      write(i , "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
      write(" & ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
      jndx = 0 
      for (j in algorithmList) {
        if (jndx != 0) {
          if (indx != jndx) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
          }
          if (j != "MoDagameSPEA2") {
            write(" & ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
          }
          else {
            write(" \\\\ ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
          }
        }
        jndx = jndx + 1
      }
      indx = indx + 1
    }
  }

  latexTableTail()
} # for problem

tabularString <-c("| l | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | p{0.15cm}   | ") 

latexTableFirstLine <-c("\\hline \\multicolumn{1}{|c|}{} & \\multicolumn{1}{c|}{MoDagameIBEA} & \\multicolumn{1}{c|}{MoDagameMOCHC} & \\multicolumn{1}{c|}{MoDagameMOCell} & \\multicolumn{1}{c|}{MoDagamePAES} & \\multicolumn{1}{c|}{MoDagameSPEA2} \\\\") 

# Step 3. Problem loop 
latexTableHeader("BerkeleyDBMemory ", tabularString, latexTableFirstLine)

indx = 0
for (i in algorithmList) {
  if (i != "MoDagameSPEA2") {
    write(i , "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
    write(" & ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)

    jndx = 0
    for (j in algorithmList) {
      for (problem in problemList) {
        if (jndx != 0) {
          if (i != j) {
            printTableLine(indicator, i, j, indx, jndx, problem)
          }
          else {
            write("  ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
          } 
          if (problem == "BerkeleyDBMemory") {
            if (j == "MoDagameSPEA2") {
              write(" \\\\ ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
            } 
            else {
              write(" & ", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
            }
          }
     else {
    write("&", "/home/josemi/Workspaces/mdeoptimiser-ws/mdeoptimiser4fm/toolsForComparison/modagame/toCompare/MODAGAME_cases/BerkeleyDBMemory/R/PROBLEMS-0-0.HV.Wilcox.tex", append=TRUE)
     }
        }
      }
      jndx = jndx + 1
    }
    indx = indx + 1
  }
} # for algorithm

  latexTableTail()

#Step 3. Writes the end of latex file 
latexTail()

