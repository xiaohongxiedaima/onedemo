import numpy as np
import operator


def create_data_set():
    group = np.array([[1.0, 1.1], [1.0, 1.0], [0, 0], [0, 0.1]])
    labels = ["A", "A", "B", "B"]

    return group, labels


def create_data_set_from_file(filename):
    fr = open(filename)
    lines = fr.readlines()

    group = np.zeros((len(lines), 3))
    labels = []

    for i in range(len(lines)):
        line = lines[i].strip()
        list_from_line = line.split("\t")
        group[i, :] = list_from_line[0: 3]
        labels.append(list_from_line[-1])

    return group, labels


def classify0(in_x, data_set, labels, k):
    data_set_size = data_set.shape[0]
    diff_mat = np.tile(in_x, (data_set_size, 1)) - data_set
    sq_diff_mat = diff_mat ** 2
    sq_distance = sq_diff_mat.sum(axis=1)
    distances = sq_distance ** 0.5
    sorted_dist_indicates = distances.argsort()
    class_count = {}
    for i in range(k):
        vote_label = labels[sorted_dist_indicates[i]]
        class_count[vote_label] = class_count.get(vote_label, 0) + 1
    sorted_class_count = sorted(class_count.items(), key=operator.itemgetter(1), reverse=True)
    return sorted_class_count[0][0]


def auto_norm(data_set):
    min_vals = data_set.min(0)
    max_vals = data_set.max(0)
    ranges = max_vals - min_vals
    # norm_data_set = np.zeros(np.shape(data_set))
    m = data_set.shape[0]
    norm_data_set = data_set - np.tile(min_vals, (m, 1))
    norm_data_set = norm_data_set / np.tile(ranges, (m, 1))

    return norm_data_set, ranges, min_vals



if __name__ == '__main__':
    group, labels = create_data_set()

    target_label = classify0(np.array([0.5, 0.5]), group, labels, 3)
    print(target_label)

