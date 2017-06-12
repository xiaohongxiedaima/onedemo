import math


def test1():
    arr = [[i / 10, math.log(i / 10, 2)] for i in range(1, 100)]
    print(arr)

if __name__ == '__main__':
    test1()
