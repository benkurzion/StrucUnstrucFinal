import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import chi2_contingency
from sklearn.linear_model import LinearRegression
from scipy import stats



data = np.loadtxt("topData.csv", delimiter=",", dtype=float) 

# Loop through each feature and test its independence against the first feature
for i in range(1, data.shape[1]):
    feature1 = data[:, 0]
    feature2 = data[:, i]
    
    # Perform the chi-square test
    statistic, p_value, dof, expected = chi2_contingency(np.vstack([feature1, feature2]))
    
    # Print the results
    print(f"Feature {i + 1}:")
    print(f"\tp-value: {p_value}")
    


corr_matrix = np.corrcoef(data, rowvar=False)

# Create a correlation plot as a heatmap
plt.figure(figsize=(8, 6))
plt.imshow(corr_matrix, cmap='viridis', interpolation='nearest')
plt.colorbar(label='Correlation coefficient')
plt.title('Correlation Plot')
plt.xticks(np.arange(len(corr_matrix)), np.arange(len(corr_matrix)))
plt.yticks(np.arange(len(corr_matrix)), np.arange(len(corr_matrix)))
plt.xlabel('Variables')
plt.ylabel('Variables')
plt.show()
