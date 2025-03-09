import numpy as np
from typing import Union


def math_expectation(numbers: Union[np.ndarray, list]) -> float:
    # Находим уникальные значения и их количество
    unique_values: np.ndarray
    counts: np.ndarray
    unique_values, counts = np.unique(numbers, return_counts=True)

    # Вычисляем вероятности
    probabilities: np.ndarray = counts / counts.sum()

    # Вычисляем математическое ожидание
    expectation: float = np.sum(unique_values * probabilities).item()

    # Округляем до 4 знаков после запятой
    expectation = round(expectation, 4)

    return expectation
