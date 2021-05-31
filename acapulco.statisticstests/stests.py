import pandas
import scipy.stats

## DOCUMENTATION FOR scipy.stats.mannwhitneyu
# https://docs.scipy.org/doc/scipy/reference/generated/scipy.stats.mannwhitneyu.html

tools = ['acapulco', 'modagame', 'satibea']


dataFiles = ['wget', 'tankwar', 'mobile_media2', 'weafqas', 'busybox-1.18.0', 'embtoolkit', 'uclinux-distribution', 'ea2468']

	

####################################################
# LOAD DATA
def case_study(name, AcapulcoDataFile, modagameDataFile, satibeaDataFile):

	print()
	print()
	print()
	print()
	print("============================")
	print()
	print("Process case study {}".format(name))
	print()
	print()

	Acapulco_data = pandas.read_csv(AcapulcoDataFile, sep=',', usecols=[0,1,2,3])
	modagame_data = pandas.read_csv(modagameDataFile, sep=',', usecols=[0,1,2,3])
	satibea_data = pandas.read_csv(satibeaDataFile, sep=',', usecols=[0,1,2,3])

	print("Acapulco data file {}".format(AcapulcoDataFile))
	print("MODAGAME data file {}".format(modagameDataFile))
	print("SATIBEA data file {}".format(satibeaDataFile))
	print()
	print()
	print("Hypervolume tests")
	print()

	# Hypervolume
	Acapulco_hv = Acapulco_data['HV']
	modagame_hv = modagame_data['HV']
	satibea_hv = satibea_data['HV']

	# Acapulco vs MODAGAME
	statistics, pvalue = scipy.stats.mannwhitneyu(Acapulco_hv, modagame_hv, False, 'greater')
	print("HV: Acapulco vs MODAGAME -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
	    print('Different distribution (reject H0) for alpha: ' + str(alpha))

	# Acapulco vs SATIBEA
	statistics, pvalue = scipy.stats.mannwhitneyu(Acapulco_hv, satibea_hv, False, 'greater')
	print("HV: Acapulco vs SATIBEA -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
		print('Different distribution (reject H0) for alpha: ' + str(alpha))

	print("--------------------------------------------------")
	print("Inversed data")
	print()
	# Acapulco vs MODAGAME
	statistics, pvalue = scipy.stats.mannwhitneyu(modagame_hv, Acapulco_hv, False, 'greater')
	print("HV: MODAGAME vs Acapulco -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
	    print('Different distribution (reject H0) for alpha: ' + str(alpha))

	# Acapulco vs SATIBEA
	statistics, pvalue = scipy.stats.mannwhitneyu(satibea_hv, Acapulco_hv, False, 'greater')
	print("HV: SATIBEA vs Acapulco -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
		print('Different distribution (reject H0) for alpha: ' + str(alpha))

	######################################################################
	# Execution time
	print()
	print("Execution time tests")
	print()
	Acapulco_time = Acapulco_data['Time (s)']
	modagame_time = modagame_data['Time (s)']
	satibea_time = satibea_data['Time (s)']

	# Acapulco vs MODAGAME
	statistics, pvalue = scipy.stats.mannwhitneyu(Acapulco_time, modagame_time, False, 'less')
	print("TIME: Acapulco vs MODAGAME -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
	    print('Different distribution (reject H0) for alpha: ' + str(alpha))

	# Acapulco vs SATIBEA
	statistics, pvalue = scipy.stats.mannwhitneyu(Acapulco_time, satibea_time, False, 'less')
	print("TIME: Acapulco vs SATIBEA -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
		print('Different distribution (reject H0) for alpha: ' + str(alpha))
		
	print("--------------------------------------------------")
	print("Inversed data")
	print()
	# Acapulco vs MODAGAME
	statistics, pvalue = scipy.stats.mannwhitneyu(modagame_time, Acapulco_time, False, 'less')
	print("TIME: MODAGAME vs Acapulco -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
	    print('Different distribution (reject H0) for alpha: ' + str(alpha))

	# Acapulco vs SATIBEA
	statistics, pvalue = scipy.stats.mannwhitneyu(satibea_time, Acapulco_time, False, 'less')
	print("TIME: SATIBEA vs Acapulco -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
		print('Different distribution (reject H0) for alpha: ' + str(alpha))	

	print("============================")
	print()

# # MODAGAME vs SATIBEA
# statistics, pvalue = scipy.stats.mannwhitneyu(modagame_hv, satibea_hv, False, 'greater')
# print("HD: MODAGAME vs SATIBEA -> stats: " + str(statistics) + " , p:" + str(pvalue))
# alpha = 0.05
# if pvalue > alpha:
# 	print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
# else:
# 	print('Different distribution (reject H0) for alpha: ' + str(alpha))


######################################################################
# TEST TO COMPARE THE RESULTS 'greater', 'less', 'two-sided'
# Acapulco_hv1 = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
# #Acapulco_data.tool = 'Acapulcoptimiser'
# statistics, pvalue = scipy.stats.mannwhitneyu(Acapulco_hv, Acapulco_hv1, False, 'greater')
# print("stats: " + str(statistics) + " , p:" + str(pvalue))
# alpha = 0.05
# if pvalue > alpha:
# 	print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
# else:
# 	print('Different distribution (reject H0) for alpha: ' + str(alpha))

def case_study2(name, AcapulcoDataFile, satibeaDataFile):

	print()
	print()
	print()
	print()
	print("============================")
	print()
	print("Process case study {}".format(name))
	print()
	print()

	Acapulco_data = pandas.read_csv(AcapulcoDataFile, sep=',', usecols=[0,1,2,3])
	satibea_data = pandas.read_csv(satibeaDataFile, sep=',', usecols=[0,1,2,3])

	print("Acapulco data file {}".format(AcapulcoDataFile))
	print("SATIBEA data file {}".format(satibeaDataFile))
	print()
	print()
	print("Hypervolume tests")
	print()

	# Hypervolume
	Acapulco_hv = Acapulco_data['HV']
	satibea_hv = satibea_data['HV']

	# Acapulco vs SATIBEA
	statistics, pvalue = scipy.stats.mannwhitneyu(Acapulco_hv, satibea_hv, False, 'greater')
	print("HV: Acapulco vs SATIBEA -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
		print('Different distribution (reject H0) for alpha: ' + str(alpha))

	print("--------------------------------------------------")
	print("Inversed data")
	print()
	# Acapulco vs SATIBEA
	statistics, pvalue = scipy.stats.mannwhitneyu(satibea_hv, Acapulco_hv, False, 'greater')
	print("HV: SATIBEA vs Acapulco -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
		print('Different distribution (reject H0) for alpha: ' + str(alpha))

	######################################################################
	# Execution time
	print()
	print("Execution time tests")
	print()
	Acapulco_time = Acapulco_data['Time (s)']
	satibea_time = satibea_data['Time (s)']

	# Acapulco vs SATIBEA
	statistics, pvalue = scipy.stats.mannwhitneyu(Acapulco_time, satibea_time, False, 'less')
	print("TIME: Acapulco vs SATIBEA -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
		print('Different distribution (reject H0) for alpha: ' + str(alpha))
		
	print("--------------------------------------------------")
	print("Inversed data")
	print()

	# Acapulco vs SATIBEA
	statistics, pvalue = scipy.stats.mannwhitneyu(satibea_time, Acapulco_time, False, 'less')
	print("TIME: SATIBEA vs Acapulco -> stats: " + str(statistics) + " , p:" + str(pvalue))
	alpha = 0.05
	if pvalue > alpha:
		print('Same distribution (fail to reject H0) for alpha: ' + str(alpha))
	else:
		print('Different distribution (reject H0) for alpha: ' + str(alpha))	

	print("============================")
	print()


folder = 'results/'
for fm in dataFiles:
	filename = "_" + fm + "_30runs_results.dat"
	acapulco_file = folder + tools[0] + filename
	modagame_file = folder + tools[1] + filename
	satibea_file = folder + tools[2] + filename
	
	case_study(fm, acapulco_file, modagame_file, satibea_file)

fm = "linux-2.6.33.3"
filename = "_" + fm + "_30runs_results.dat"
acapulco_file = folder + tools[0] + filename
satibea_file = folder + tools[2] + filename
case_study2(fm, acapulco_file, satibea_file)

fm = "automotive2_1"
filename = "_" + fm + "_30runs_results.dat"
acapulco_file = folder + tools[0] + filename
satibea_file = folder + tools[2] + filename
case_study2(fm, acapulco_file, satibea_file)

