import matplotlib.pyplot as plt
import numpy as np


# 散点图
def scatter():
    plt.subplot(321)
    x = np.random.randn(1000)
    y = x + np.random.randn(1000) * 0.5

    # s表示面积，marker表示图形
    plt.scatter(x, y, s=5, marker='<')
    return


# 直方图
def hist():
    plt.subplot(322)
    mu = 100
    sigma = 20
    # 样本数量
    x = mu + sigma * np.random.randn(20000)
    # bins显示有几个直方,normed是否对数据进行标准化
    plt.hist(x, bins=100, color='green', normed=True)


# 条形图
def bar():
    plt.subplot(323)
    y = [20, 10, 30, 25, 15]

    index = np.arange(5)

    plt.bar(left=index, height=y, color='green', width=0.5)


# 折线图
def plot():
    plt.subplot(324)
    x = np.linspace(-10, 10, 100)

    y = x ** 3

    plt.plot(x, y, linestyle='--', color='green', marker='<')
    plt.legend(['Normal'])

    plt.grid(True, color='g', linestyle='--', linewidth='1')


# 箱形图
def boxplot():
    plt.subplot(325)
    np.random.seed(100)

    data = np.random.normal(size=(1000, 4), loc=0, scale=1)

    labels = ['A', 'B', 'C', 'D']

    plt.boxplot(data, labels=labels)


# 饼状图
def pie():
    plt.subplot(326)
    labels = 'A', 'B', 'C', 'D'

    fracs = [15, 30, 45, 10]
    # 使x y轴比例相同
    # plt.axes(aspect=1)
    # 突出某一部分区域
    # explode = [0, 0.05, 0, 0]
    # autopct显示百分比
    plt.pie(radius=0.5, x=fracs, labels=labels)


if __name__ == '__main__':
    scatter()
    hist()
    bar()
    plot()
    boxplot()
    pie()
    plt.show()
