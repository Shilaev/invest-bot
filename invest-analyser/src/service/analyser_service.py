import numpy as np


def math_expectation(numbers: np.ndarray):
    # Находим уникальные значения и их количество
    unique_values, counts = np.unique(numbers, return_counts=True)

    # Вычисляем вероятности
    probabilities = counts / counts.sum()

    # Вычисляем математическое ожидание
    expectation = np.sum(unique_values * probabilities)

    return expectation, probabilities
