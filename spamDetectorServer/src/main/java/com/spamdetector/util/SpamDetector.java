package com.spamdetector.util;

import com.spamdetector.domain.TestFile;
import com.fasterxml.jackson.databind.ObjectMapper; // Make sure this import is included

import java.io.*;
import java.util.*;

public class SpamDetector {

    private List<TestFile> testFiles;

    /**
     * Calculate the probability of an email being spam given the probabilities of individual words.
     *
     * @param wordSpamProbabilities List of probabilities of individual words being spam
     * @return Probability of the email being spam
     */
    private double calculateSpamProbability(Map<String, Double> wordSpamProbabilities, String word) {
        double probWordGivenSpam = wordSpamProbabilities.getOrDefault(word, 0.0); // Probability of word given spam
        double probWordGivenHam = 1.0 - probWordGivenSpam; // Probability of word given ham
    
        probWordGivenSpam = Math.max(probWordGivenSpam, Double.MIN_VALUE);
        probWordGivenHam = Math.max(probWordGivenHam, Double.MIN_VALUE);
    
        return probWordGivenSpam / (probWordGivenSpam + probWordGivenHam);
    }

    /**
     * Train and test the spam detector using the files in the main directory.
     *
     * @param mainDirectory The main directory containing the testing data
     * @return List of TestFile objects representing the test results
     */
    public List<TestFile> trainAndTest(File mainDirectory) {
        if (mainDirectory == null || !mainDirectory.isDirectory()) {
            System.err.println("Invalid main directory.");
            return null;
        }

        // Initialize maps to store word frequencies
        Map<String, Integer> trainHamFreq = new HashMap<>();
        Map<String, Integer> trainSpamFreq = new HashMap<>();

        // Read every single file in each of 'ham' and 'spam' subdirectories for training
        processDirectoryTrain(new File(mainDirectory, "train/ham"), false, trainHamFreq, trainSpamFreq);
        processDirectoryTrain(new File(mainDirectory, "train/spam"), true, trainHamFreq, trainSpamFreq);

        // Calculate probabilities (Pr(Wi|S) and Pr(Wi|H))
        Map<String, Double> wordProbabilities = new HashMap<>();
        int totalHamFiles = trainHamFreq.values().stream().mapToInt(Integer::intValue).sum();
        int totalSpamFiles = trainSpamFreq.values().stream().mapToInt(Integer::intValue).sum();
        for (String word : trainHamFreq.keySet()) {
            double probHam = (double) trainHamFreq.get(word) / totalHamFiles;
            double probSpam = (double) trainSpamFreq.getOrDefault(word, 0) / totalSpamFiles;
            wordProbabilities.put(word, probSpam / (probSpam + probHam)); // Pr(S|Wi)
        }

        // Test the spam detector using the files in 'test' subdirectories
        testFiles = new ArrayList<>();
        processDirectoryTest(new File(mainDirectory, "test/ham"), false, wordProbabilities);
        processDirectoryTest(new File(mainDirectory, "test/spam"), true, wordProbabilities);

        return testFiles;
    }

    /**
     * Process files in the given directory to calculate spam probabilities.
     *
     * @param directory             The directory to process
     * @param isSpam                True if the directory contains spam files, False otherwise
     * @param wordSpamProbabilities Probabilities of individual words being spam
     */
    private void processDirectoryTest(File directory, boolean isSpam, Map<String, Double> wordSpamProbabilities) {
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Read every word in the file and calculate P(S|F)
                        double spamProbability = calculateSpamProbability(wordSpamProbabilities);
                        // Determine the actual class of each file
                        String actualClass = isSpam ? "Spam" : "Ham";
                        // Using the filename, P(S|F), and the actual class, populate the table
                        testFiles.add(new TestFile(file.getName(), spamProbability > 0.5, actualClass));
                    }
                }
            }
        }
    }

    /**
     * Process files in the given directory to calculate word frequencies.
     *
     * @param directory     The directory to process
     * @param isSpam        True if the directory contains spam files, False otherwise
     * @param trainHamFreq  Map to store word frequencies in ham emails
     * @param trainSpamFreq Map to store word frequencies in spam emails
     */
    private void processDirectoryTrain(File directory, boolean isSpam, Map<String, Integer> trainHamFreq, Map<String, Integer> trainSpamFreq) {
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            BufferedReader reader = new BufferedReader(new FileReader(file));
                            String line;
                            Set<String> uniqueWords = new HashSet<>();
                            while ((line = reader.readLine()) != null) {
                                // Tokenize the line into words
                                String[] words = line.toLowerCase().split("\\s+");
                                // Add unique words to the set
                                uniqueWords.addAll(Arrays.asList(words));
                            }
                            reader.close();

                            // Update word frequencies based on the folder (spam/ham)
                            Map<String, Integer> currentFreqMap;
                            if (isSpam) {
                                currentFreqMap = trainSpamFreq;
                            } else {
                                currentFreqMap = trainHamFreq;
                            }
                            for (String word : uniqueWords) {
                                currentFreqMap.put(word, currentFreqMap.getOrDefault(word, 0) + 1);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Generate a JSON file containing the test results.
     *
     * @param filePath Path to the output JSON file
     */
    public void generateJsonFile(String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Writing the testFiles list to a JSON file at the specified filePath
            mapper.writeValue(new File(filePath), testFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
