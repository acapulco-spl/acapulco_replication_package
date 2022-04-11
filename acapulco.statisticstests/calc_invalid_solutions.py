import csv
import os

TOOLS = ['acapulco', 'modagame', 'satibea']


DATA_FILES = ['wget', 'tankwar', 'mobile_media2', 'weafqas', 'busybox-1.18.0', 'embtoolkit', 'uclinux-distribution', 'ea2468', 'linux-2.6.33.3', 'automotive2_1']


def get_last_result_file(tool_name, fm_name) -> str:
    result_files = []
    for subdir, dirs, files in os.walk(os.path.join('results', fm_name)):
        if tool_name in subdir:
            for file in files:
                result_files.append(os.path.join(subdir, file))
    if result_files:
        latest_file = max(result_files, key=os.path.getctime)
    else:
        return None
    return latest_file


def calc_invalid_solutions(data_file) -> tuple[int, float]:
    with open(data_file) as file:
        lines = file.readlines()
    last_line = lines[-1]
    split_line = last_line.split('"')
    approximation_set = split_line[3]
    solutions = approximation_set.split(', ')
    invalid_solutions = sum(s[2] != '0' if s[1] == '[' else s[1] != '0' for s in solutions)
    return (invalid_solutions, invalid_solutions/len(solutions)*100)


if __name__ == '__main__':
    for fm in DATA_FILES:
        print("============================")
        print("Process case study {}".format(fm))

        for tool in TOOLS:
            filename = get_last_result_file(tool, fm)
            if filename is not None:
                invalid_solutions, percentage = calc_invalid_solutions(filename)
                print(f'{tool}: {invalid_solutions} ({percentage}%)')
