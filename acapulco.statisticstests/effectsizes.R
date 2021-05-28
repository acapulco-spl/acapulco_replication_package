library(coin)

setwd("C:\\Users\\danie\\git\\acapulco\\acapulco.statisticstests")

#install.packages("effsize")
library(effsize)

cases <- c(
"wget",
"tankwar",
"mobile_media2",
"weafqas",
"busybox-1.18.0",
"ea2468",
"embtoolkit",
"uclinux-distribution",
"linux-2.6.33.3",
"automotive2_1"
)

print(paste("Effect size computations for HV"))

for(case in cases) {
	colname = "HV"
	print(paste("Case:",case))
	
	acapulco_data = read.csv(paste("results//acapulco_",case,"_30runs_results.dat", sep=""))
	satibea_data = read.csv(paste("results//satibea_",case,"_30runs_results.dat", sep=""))
	
	print("Comparison: acapulco vs. satibea")
	print(VD.A(unlist(acapulco_data[col]),unlist(satibea_data[col])))
	
	if (case != "automotive2_1" && case != "linux-2.6.33.3") { 
		modagame_data = read.csv(paste("results//modagame_",case,"_30runs_results.dat", sep=""))
		print("Comparison:  acapulco vs. modagame")
		print(VD.A(unlist(acapulco_data[col]),unlist(modagame_data[col])))
	}
}



print(paste("Effect size computations for Time"))

for(case in cases) {
	colname = "Time .s.."
	print(paste("Case:",case))
	
	acapulco_data = read.csv(paste("results//acapulco_",case,"_30runs_results.dat", sep=""))
	satibea_data = read.csv(paste("results//satibea_",case,"_30runs_results.dat", sep=""))
	
	print("Comparison: acapulco vs. satibea")
	print(VD.A(unlist(satibea_data[col]),unlist(acapulco_data[col])))
	
	if (case != "automotive2_1" && case != "linux-2.6.33.3") { 
		modagame_data = read.csv(paste("results//modagame_",case,"_30runs_results.dat", sep=""))
		print("Comparison:  acapulco vs. modagame")
		print(VD.A(unlist(modagame_data[col]),unlist(acapulco_data[col])))
	}
}


