import numpy as np
from typing import Union


def math_expectation(numbers: Union[np.ndarray, list]) -> float:
    # Находим уникальные значения и их количество
    unique_values, counts = np.unique(numbers, return_counts=True)

    # Вычисляем вероятности
    probabilities = counts / counts.sum()

    # Вычисляем математическое ожидание
    expectation = np.sum(unique_values * probabilities)

    # Округляем до 4 знаков после запятой
    expectation = round(expectation.item(), 4)

    return expectation
