# Assignment 01 - Spam Detector
CSCI 2020U: Software Systems Development and Integration

## Developers and Associated Github Links
-	Amaan Ahmed: https://github.com/amaan135
-	Saffron Birch: https://github.com/SaffyTaffy
-	Mujtaba Chaudhry: https://github.com/mujtabach2
-	Aryan Ved: https://github.com/ak007spongebob

## Overview
In today’s digital age, the influx of unwanted emails, commonly known as “spam,” has become a nuisance for many users. To combat this problem, we developed a spam detection program to classify whether a given email is legitimate or fraudulent. In other words, whether an email is “ham” or “spam.” The goal of this assignment is to train a model that can accurately distinguish between the two categories. By employing a unigram approach, we can calculate the probabilities based on the count and frequency of unique words found within the dataset.

## How To Run
1.	Install git on the device where you wish to access the repository.
2.	Clone the necessary files using one of the following methods:
>To clone using HTTPS, copy the link under “<> Code” in the upper right-hand side of the page.
> 
>To clone using an SSH key, copy the SSH link under the same tab.
3.	Once the link is copied, open git bash, or your respective terminal where git has been installed, and change into the directory where you wish to store the repository.
4.	Type `git clone`, paste your copied link and press Enter.
5.	If the repository is being cloned using HTTPS Authentication, enter your credentials when prompted. If using SSH Authentication, enter your unique SSH key when prompted.
6.	Once the repository is cloned to your directory, open the program using your preferred IDE. 
7.	When the program is loaded, confirm a GlassFish Server configuration has been added before running.
8.	Once a configuration has been established, run the server, to have the website open in your default (or otherwise specified) browser.

Certainly, let's delve deeper into the explanation of changes and their rationales:

## Training Enhancement:
### Refining Word Frequency Thresholds
1. **Introduction of Frequency Thresholds:** We're implementing frequency thresholds to better train the model by filtering out less informative words. This involves setting minimum and maximum thresholds for word frequencies to exclude very common or rare words.

**Rationale:**
- **Reducing Noise:** Common words like "the" or "and" appear frequently across spam and non-spam emails, adding noise. Excluding them helps the model focus on more relevant features.
- **Improving Efficiency:** Rare words provide little information for the model to learn from. By excluding them, we streamline the learning process, focusing on words more indicative of spam.

### Spam Classification Optimization:

1. **Dynamic Threshold Setting:** Instead of a fixed threshold, we propose determining an optimal threshold based on context and needs. This involves considering the costs of misclassification, like missing spam (false negatives) versus wrongly marking non-spam as spam (false positives).
  
2. **ROC Curve Analysis:** We'll use techniques like ROC curve analysis to identify the best threshold by balancing sensitivity (true positives) and specificity (true negatives).

**Rationale:**
- **Context Sensitivity:** Different applications have different tolerances for false positives and negatives. By setting thresholds based on context, we can prioritize what matters most.
- **Cost Consideration:** Factoring in the costs of misclassification helps optimize the threshold for overall performance.
- **Trade-off Analysis:** ROC curve analysis helps us understand the performance trade-offs at different thresholds, guiding us to choose the most suitable one.[4]

### Further Exploration with More Time:

Given additional time, we'd conduct extensive experiments to fine-tune frequency and classification thresholds. This would involve evaluating various cutoff points to optimize model performance. We'd also rigorously test the model's performance across different scenarios using cross-validation techniques and real-world data. Continuous refinement based on insights gained from experimentation would lead to a more accurate spam detection model.

## References 
[1] https://en.wikipedia.org/wiki/Bag-of-words_model 

[2] https://en.wikipedia.org/wiki/Naive_Bayes_spam_filtering 

[3] https://www.w3schools.com/w3css/w3css_tabulators.asp

[4] https://www.analyticsvidhya.com/blog/2020/06/auc-roc-curve-machine-learning/#:~:text=The%20Area%20Under%20the%20Curve,the%20positive%20and%20negative%20classes
