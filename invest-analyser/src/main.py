import numpy as np

from src.service.analyser_service import math_expectation

if __name__ == '__main__':
    values = np.array([1500, 1650, 1630, 1600, 1550, 1810, 1910, 2000, 2100, 2400, 2500, 2150, 2350, 2130, 1950, 2060])

    math_e, probs = math_expectation(values)

    for i, (value, props) in enumerate(zip(values, probs)):
        print(f"X_{i}: {value} | P_{i}: {props}")
        i += 1

    print(f"Мат. ожидание: {math_e}")
