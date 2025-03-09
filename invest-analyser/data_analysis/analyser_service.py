import numpy as np


def math_expectation(numbers):
    # Находим уникальные значения и их количество
    unique_values, counts = np.unique(numbers, return_counts=True)

    # Вычисляем вероятности
    probabilities = counts / counts.sum()

    # Вычисляем математическое ожидание
    expectation = np.sum(unique_values * probabilities)

    # Округляем до 4 знаков после запятой
    expectation = round(expectation, 4)

    return expectation
