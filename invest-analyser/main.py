import numpy as np


def math_expectation(numbers):
    # Находит уникальные вхождения и возвращает их количество в массиве
    unique_values, counts = np.unique(numbers, return_counts=True)

    # Делим количество вхождений числа на сумму всех чисел и записываем каждое вычисление в массив
    probabilities = counts / np.sum(counts)

    # Умножаю каждое число на его вероятность появления в массиве
    value_props = []

    for value, prop in zip(unique_values, probabilities):
        value_props.append(value * prop)

    # Суммирую предыдущие вычисления, чтобы получить мат ожидание
    value_props = np.array(value_props)
    math_expectation = np.sum(value_props)
    return math_expectation


if __name__ == '__main__':
    values = np.array([1500, 1650, 1630, 1600, 1550])

    print(math_expectation(values))

