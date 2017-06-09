from app.knn import knn
import math
import numpy as np
import matplotlib
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches


def show_scatter(group, labels_colors, metrics):
    # 字体设置
    # /usr/share/fonts/opentype/noto/NotoSansCJK-Regular.ttc
    zhfont = matplotlib.font_manager.FontProperties(
        fname='/usr/share/fonts/adobe-source-han-serif/SourceHanSerifCN-Regular.otf')

    # 指标对比图形个数
    fg_num = 1
    metrics_len = len(metrics)
    for i in range(1, metrics_len + 1):
        fg_num = fg_num * i

    fg_num = math.ceil(fg_num / 2 / 2)

    # legend
    legend = {}
    for label_color in labels_colors:
        legend[label_color[0]] = label_color[1]
    legend = np.array([[key, value] for key, value in legend.items()])

    recs = [mpatches.Rectangle((0, 0), 1, 1, fc=color) for color in legend[:, 1]]

    # 散点图
    index = 1
    for i in range(0, metrics_len - 1):
        for j in range(i + 1, metrics_len):
            plt.subplot(fg_num * 100 + 20 + index)
            index = index + 1
            plt.scatter(group[:, i], group[:, j], c=labels_colors[:, 1])
            ax = plt.gca()
            ax.set_xlabel(metrics[i], fontproperties=zhfont)
            ax.set_ylabel(metrics[j], fontproperties=zhfont)
            plt.legend(recs, legend[:, 0], loc=1)

    plt.show()


if __name__ == '__main__':
    filename = "/home/xiaohong/code/onedemo/onedemo-ml/resources/knn/datingTestSet.txt"
    group, labels = knn.create_data_set_from_file(filename)
    print(group)
    print(labels)
    metrics = {
        0: "每周航班里程数",
        1: "玩游戏所耗时间百分比",
        2: "每周消耗的冰淇淋公升数"
    }

    labels_colors = []
    for label in labels:
        if label == "largeDoses":
            color = [label, "#1E90FF"]
        elif label == "smallDoses":
            color = [label, "#00BFFF"]
        elif label == "didntLike":
            color = [label, "#87CEFA"]
        else:
            color = [label, 'r']
        labels_colors.append(color)
    show_scatter(group, np.array(labels_colors), metrics)