from numpy import *

if __name__ == '__main__':
    rand_mat = mat(random.rand(4, 4))
    inv_rand_mat = rand_mat.I
    print(rand_mat)
    print(inv_rand_mat)
