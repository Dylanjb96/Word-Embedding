package ie.atu.sw.embeddings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import ie.atu.sw.console.ConsoleColor;
import ie.atu.sw.console.ConsoleLoadingMeter;
import ie.atu.sw.util.Array;
import ie.atu.sw.util.VectorOperations;
import ie.atu.sw.util.VectorSimilarityMetrics;

/**
 * Class to load and interact with a words-embeddings file.
 * 
 * This class includes:
 * - CSV format detection
 * - Word vector operations (search, add, subtract, multiply, divide)
 * - Similarity algorithms to find similar/dissimilar words
 */
public class WordsEmbeddingsSearch {

    // The name of the file containing the word embeddings
    private String fileName;
    // The delimiter used in the CSV file
    private String delimiter;

    // Array of words loaded from the embeddings file
    private String[] words;
    // The number of words loaded from the embeddings file
    private int numberOfWords;

    // 2D array containing the embeddings (vectors) for the words
    private double[][] embeddings;
    // The number of features (dimensions) for each embedding
    private int numberOfFeatures;

    // Array to store the previously found similar words
    private String[] previousSimilarWords;
    // Array to store the similarity scores of the previously found similar words
    private double[] previousSimilarWordsScores;

    // The similarity metrics used to find similar/dissimilar words
    private VectorSimilarityMetrics Vectorsimilarity = VectorSimilarityMetrics.COSINE_SIMILARITY;

    /**
     * Creates a WordsEmbeddings instance by loading a words-embeddings file from
     * the provided file name.
     * 
     * Stores the file name.
     * Loads the words and word vectors from the specified file name.
     * 
     * @param fileName - The file name to load words-embeddings from.
     * @throws Exception if an error occurs while loading the embeddings file.
     */
    public WordsEmbeddingsSearch(String fileName) throws Exception {
        setFileName(fileName);
    }

    /**
     * Get the loaded words-embeddings file name.
     * 
     * @return the name of the loaded words-embeddings file.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set and load a words-embeddings file name.
     * 
     * @param fileName the name of the file to load words-embeddings from
     * @throws Exception if an error occurs while loading the words-embeddings
     */
    private void setFileName(String fileName) throws Exception {
        this.fileName = fileName;
        setWordsAndEmbeddings();
    }

    /**
     * Get the words array, which was loaded from the words-embeddings file.
     * 
     * @return the words array, which was loaded from the words-embeddings file
     */
    public String[] getWords() {
        return words;
    }

    /**
     * Get the number of words loaded from the words-embeddings file.
     * 
     * @return the number of words loaded from the words-embeddings file
     */
    public int getNumberOfWords() {
        return numberOfWords;
    }

    /**
     * Get the array of words-embeddings arrays.
     * 
     * @return the words-embeddings array loaded from the words-embeddings file
     */
    public double[][] getEmbeddings() {
        return embeddings;
    }

    /**
     * Get the number of data points/features that were loaded from the
     * words-embeddings file.
     * 
     * @return the number of data points/features that were loaded from the
     *         words-embeddings file
     */
    public int getNumberOfFeatures() {
        return numberOfFeatures;
    }

    /**
     * Get the current similarity metric, e.g., dot product or cosine similarity.
     * 
     * @return the current similarity metric, which defaults to cosine similarity
     */
    public VectorSimilarityMetrics getVectorSimilarity() {
        return Vectorsimilarity;
    }

    /**
     * Set the current similarity metric to be used.
     * 
     * @param Vectorsimilarity the similarity metric to be set, e.g., dot product
     *                         or cosine similarity
     */
    public void setVectorSimilarity(VectorSimilarityMetrics Vectorsimilarity) {
        this.Vectorsimilarity = Vectorsimilarity;
    }

    /**
     * Retrieve the array of words from the most recent similarity or dissimilarity
     * search.
     * 
     * @return a string array containing the words from the latest search for
     *         similar or dissimilar words
     */
    public String[] getPreviousSimilarWords() {
        return previousSimilarWords;
    }

    /**
     * Retrieve the array of scores corresponding to the most recent similarity or
     * dissimilarity search.
     * 
     * @return a double array containing the scores of the words from the latest
     *         search for similar or dissimilar words
     */
    public double[] getPreviousSimilarWordsScores() {
        return previousSimilarWordsScores;
    }

    /**
     * Retrieve the index of a specified word from the array of words loaded from
     * the words-embeddings file.
     * 
     * This method searches through the array of words to find the index of the
     * given word.
     * 
     * @param word the word whose index is to be found
     * @return the index of the specified word in the array of words
     * @throws Exception if the word is not found in the array
     */
    public int getWordIndex(String word) throws Exception {
        for (int i = 0; i < this.numberOfWords; i++)
            if (this.words[i].equals(word))
                return i;

        throw new Exception("Cannot find word: '" + word + "'");
    }

    /**
     * Retrieve the word-embedding array for a specified word from the
     * words-embeddings file.
     * 
     * This method locates the word in the array of words and returns its
     * corresponding word-embedding array.
     * 
     * @param word the word for which the embedding is to be retrieved
     * @return a double array representing the word's embedding
     * @throws Exception if the word is not found in the words array
     */
    public double[] getWordEmbedding(String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return this.embeddings[wordIndex];
    }

    /**
     * Perform a vector addition operation using the vector representations of two
     * words.
     * 
     * This method retrieves the word embeddings for the specified words and
     * performs vector addition
     * on these embeddings. It returns the result of the addition operation.
     * 
     * @param word1 the first word whose vector embedding will be used in the
     *              operation
     * @param word2 the second word whose vector embedding will be used in the
     *              operation
     * @return a double array representing the result of adding the two word
     *         embeddings
     * @throws Exception if either word is not found in the loaded words-embeddings
     *                   file, or if the word
     *                   vector arrays are not of the same length
     */
    public double[] add(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return VectorOperations.add(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    /**
     * Perform a vector addition operation using the vector representation of a word
     * and an additional vector.
     * 
     * This method retrieves the vector embedding for the specified word and adds it
     * to the provided
     * additional vector. It returns the result of the addition operation.
     * 
     * @param word      the word whose vector embedding will be used in the
     *                  operation
     * @param embedding the vector representation to be added to the word's
     *                  embedding
     * @return a double array representing the result of adding the word's embedding
     *         to the provided vector
     * @throws Exception if the word is not found in the loaded words-embeddings
     *                   file, or if the vector arrays
     *                   (the word's embedding and the provided vector) are not of
     *                   the same length
     */
    public double[] add(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return VectorOperations.add(this.embeddings[wordIndex], embedding);
    }

    /**
     * Perform a vector addition operation using a provided vector and the vector
     * representation of a specified word.
     * 
     * This method takes a vector and adds it to the vector embedding of the
     * specified word, which is retrieved
     * from the loaded words-embeddings file. It returns the result of the addition
     * operation.
     * 
     * @param embedding a vector representation to be added to the word's embedding
     * @param word      a string whose vector embedding will be used in the
     *                  operation
     * @return a double array representing the result of adding the provided vector
     *         to the word's embedding
     * @throws Exception if the word is not found in the loaded words-embeddings
     *                   file, or if the vector arrays
     *                   (the provided vector and the word's embedding) are not of
     *                   the same length
     */
    public double[] add(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return VectorOperations.add(embedding, this.embeddings[wordIndex]);
    }

    /**
     * Perform a vector addition operation on two vector representations.
     * 
     * This method adds two vectors together. Both vectors are expected to represent
     * word embeddings
     * or other similar vector representations. The method returns the result of
     * this vector addition.
     * 
     * @param embedding1 a vector representation to be added
     * @param embedding2 another vector representation to be added
     * @return a double array representing the result of adding the two input
     *         vectors
     * @throws Exception if the two vector arrays are not of the same length
     */
    public double[] add(double[] embedding1, double[] embedding2) throws Exception {
        return VectorOperations.add(embedding1, embedding2);
    }

    /**
     * Perform a vector subtraction operation using the embeddings of two words.
     * 
     * This method subtracts the vector representation of one word from the vector
     * representation of another word. Both words must be present in the loaded
     * words-embeddings file, and their corresponding vector representations must
     * have the same length.
     * 
     * @param word1 the first word whose vector representation will be subtracted
     *              from
     * @param word2 the second word whose vector representation will be subtracted
     * @return a double array representing the result of subtracting the vector of
     *         word2 from the vector of word1
     * @throws Exception if either word is not found in the embeddings file, or if
     *                   the vector arrays of the two words are not of the same
     *                   length
     */
    public double[] subtract(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return VectorOperations.subtract(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    /**
     * Perform a vector subtraction operation between the embedding of a word and a
     * given vector.
     * 
     * This method subtracts the provided vector representation from the vector
     * representation
     * of a specified word. The word must be present in the loaded words-embeddings
     * file, and
     * the provided vector must have the same length as the word's vector.
     * 
     * @param word      the word whose vector representation will be subtracted from
     * @param embedding the vector representation to subtract from the word's vector
     * @return a double array representing the result of subtracting the provided
     *         vector
     *         from the vector of the specified word
     * @throws Exception if the word is not found in the embeddings file, or if the
     *                   provided
     *                   vector and the word's vector are not of the same length
     */
    public double[] subtract(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return VectorOperations.subtract(this.embeddings[wordIndex], embedding);
    }

    /**
     * Perform a vector subtraction operation between a given vector and the vector
     * representation of a specified word.
     * 
     * This method subtracts the vector representation of a specified word from the
     * provided vector. The word must be present
     * in the loaded words-embeddings file, and the provided vector must have the
     * same length as the word's vector.
     * 
     * @param embedding the vector representation to be subtracted from the word's
     *                  vector
     * @param word      the word whose vector representation will be subtracted from
     *                  the provided vector
     * @return a double array representing the result of subtracting the vector of
     *         the specified word from the provided vector
     * @throws Exception if the word is not found in the embeddings file, or if the
     *                   provided vector and the word's vector are
     *                   not of the same length
     */
    public double[] subtract(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return VectorOperations.subtract(embedding, this.embeddings[wordIndex]);
    }

    /**
     * Perform a vector subtraction operation between two vector representations.
     * 
     * This method subtracts the second vector representation from the first vector
     * representation. Both vectors must have
     * the same length to perform the subtraction.
     * 
     * @param embedding1 the first vector representation from which the second
     *                   vector will be subtracted
     * @param embedding2 the second vector representation to be subtracted from the
     *                   first vector
     * @return a double array representing the result of the vector subtraction
     * @throws Exception if the two vectors have different lengths, which makes the
     *                   operation invalid
     */
    public double[] subtract(double[] embedding1, double[] embedding2) throws Exception {
        return VectorOperations.subtract(embedding1, embedding2);
    }

    /**
     * Perform a vector multiplication operation between two vector representations
     * of words.
     * 
     * This method multiplies the vector representation of the second word with the
     * vector representation of the first word.
     * Both vectors must have the same length to perform the element-wise
     * multiplication.
     * 
     * @param word1 the first word whose vector representation will be multiplied
     * @param word2 the second word whose vector representation will be used to
     *              multiply with the first word's vector
     * @return a double array representing the result of the element-wise vector
     *         multiplication
     * @throws Exception if either of the words is not found in the embeddings or if
     *                   the vector arrays of the words have different lengths
     */
    public double[] multiply(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return VectorOperations.multiply(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    /**
     * Perform a vector multiplication operation between a word's vector
     * representation and another given vector.
     * 
     * This method multiplies the vector representation of the specified word with a
     * provided vector.
     * The word's vector and the provided vector must have the same length to
     * perform the element-wise multiplication.
     * 
     * @param word      the word whose vector representation will be multiplied
     * @param embedding the vector representation that will be used to multiply with
     *                  the word's vector
     * @return a double array representing the result of the element-wise vector
     *         multiplication
     * @throws Exception if the word is not found in the embeddings or if the word's
     *                   vector and the provided vector have different lengths
     */
    public double[] multiply(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return VectorOperations.multiply(this.embeddings[wordIndex], embedding);
    }

    /**
     * Perform a vector multiplication operation between a provided vector and the
     * vector representation of a specified word.
     * 
     * This method multiplies the given vector with the vector representation of the
     * specified word.
     * The word's vector and the provided vector must have the same length to ensure
     * element-wise multiplication is performed correctly.
     * 
     * @param embedding the vector representation that will be multiplied with the
     *                  vector of the specified word
     * @param word      the word whose vector representation will be used in the
     *                  multiplication
     * @return a double array representing the result of the element-wise vector
     *         multiplication
     * @throws Exception if the word is not found in the embeddings or if the
     *                   lengths of the vectors (word's vector and the provided
     *                   vector) do not match
     */
    public double[] multiply(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return VectorOperations.multiply(embedding, this.embeddings[wordIndex]);
    }

    /**
     * Perform element-wise multiplication between two vector representations.
     * 
     * This method multiplies each corresponding element of the two provided
     * vectors.
     * Both vectors must have the same length to ensure that the multiplication is
     * performed correctly.
     * 
     * @param embedding1 the first vector representation to be multiplied
     * @param embedding2 the second vector representation to be multiplied
     * @return a double array representing the result of the element-wise
     *         multiplication
     * @throws Exception if the lengths of the input vectors are not the same
     */
    public double[] multiply(double[] embedding1, double[] embedding2) throws Exception {
        return VectorOperations.multiply(embedding1, embedding2);
    }

    /**
     * Perform element-wise division between two vector representations of words.
     * 
     * This method divides each corresponding element of the two provided vectors.
     * Both vectors must have the same length to ensure that the division is
     * performed correctly.
     * The operation is conducted element-wise where each element in the first
     * vector is divided by
     * the corresponding element in the second vector.
     * 
     * @param word1 the first word whose vector representation is used as the
     *              numerator in the division
     * @param word2 the second word whose vector representation is used as the
     *              denominator in the division
     * @return a double array representing the result of the element-wise division
     *         of the two vectors
     * @throws Exception if either of the words is not found in the loaded
     *                   word-embeddings file,
     *                   or if the vectors associated with the words are not of the
     *                   same length
     */
    public double[] divide(String word1, String word2) throws Exception {
        int wordIndex1 = getWordIndex(word1);
        int wordIndex2 = getWordIndex(word2);
        return VectorOperations.divide(this.embeddings[wordIndex1], this.embeddings[wordIndex2]);
    }

    /**
     * Perform element-wise division between the vector representation of a word
     * and a provided vector (embedding).
     * 
     * This method divides each element of the vector representation of the
     * specified word
     * by the corresponding element in the provided vector (embedding). Both vectors
     * must
     * have the same length to ensure the division is performed correctly.
     * 
     * @param word      the word whose vector representation is used as the
     *                  numerator in the division
     * @param embedding the vector (embedding) used as the denominator in the
     *                  division
     * @return a double array representing the result of the element-wise division
     *         of the word's vector
     *         by the provided vector
     * @throws Exception if the specified word is not found in the loaded
     *                   words-embeddings file,
     *                   or if the vector associated with the word and the provided
     *                   embedding are
     *                   not of the same length
     */
    public double[] divide(String word, double[] embedding) throws Exception {
        int wordIndex = getWordIndex(word);
        return VectorOperations.divide(this.embeddings[wordIndex], embedding);
    }

    /**
     * Perform element-wise division between a provided vector (embedding) and
     * the vector representation of a specified word.
     * 
     * This method divides each element of the provided vector (embedding) by the
     * corresponding element in the vector representation of the specified word.
     * Both vectors must have the same length to ensure the division is performed
     * correctly.
     * 
     * @param embedding the vector to be divided by the vector representation of the
     *                  specified word
     * @param word      the word whose vector representation is used as the divisor
     *                  in the division
     * @return a double array representing the result of the element-wise division
     *         of the provided vector
     *         by the word's vector
     * @throws Exception if the specified word is not found in the loaded
     *                   words-embeddings file,
     *                   or if the provided vector and the word's vector are not of
     *                   the same length
     */
    public double[] divide(double[] embedding, String word) throws Exception {
        int wordIndex = getWordIndex(word);
        return VectorOperations.divide(embedding, this.embeddings[wordIndex]);
    }

    /**
     * Perform element-wise division between two vector representations of words.
     * 
     * This method divides each element of the first vector (`embedding1`) by the
     * corresponding element of the second vector (`embedding2`). Both vectors must
     * have the same length to ensure the division is performed correctly.
     * 
     * @param embedding1 the vector to be divided
     * @param embedding2 the vector to divide by
     * @return a double array representing the result of the element-wise division
     *         of `embedding1`
     *         by `embedding2`
     * @throws Exception if the vectors `embedding1` and `embedding2` are not of the
     *                   same length,
     *                   which would prevent proper element-wise division
     */
    public double[] divide(double[] embedding1, double[] embedding2) throws Exception {
        return VectorOperations.divide(embedding1, embedding2);
    }

    /**
     * Retrieve a specified number of words that are most similar to a given word
     * based on the loaded word embeddings.
     * 
     * This method finds the most similar words to the specified word by using the
     * currently set similarity algorithm.
     * The number of similar words to return is determined by the `numValues`
     * parameter.
     * 
     * @param word      the target word for which similar words are to be found
     * @param numValues the number of similar words to return
     * @return an array of strings containing the words that are most similar to the
     *         specified word
     * @throws Exception if an error occurs during the retrieval of similar words,
     *                   such as if the word is not found
     */
    public String[] getSimilarWords(String word, int numValues) throws Exception {
        return getSimilarWords(word, numValues, true);
    }

    /**
     * Retrieve a specified number of words that are most similar to a given vector
     * representation of a word.
     * 
     * This method finds the most similar words to the provided vector embedding by
     * using the currently set similarity algorithm.
     * The number of similar words to return is specified by the `numValues`
     * parameter.
     * 
     * @param embedding the vector representation of the word for which similar
     *                  words are to be found
     * @param numValues the number of similar words to retrieve
     * @return an array of strings containing the words that are most similar to the
     *         provided vector representation
     * @throws Exception if an error occurs during the retrieval of similar words,
     *                   such as if the vector is not valid
     */
    public String[] getSimilarWords(double[] embedding, int numValues) throws Exception {
        return getSimilarWords(embedding, numValues, true);
    }

    /**
     * Retrieve a specified number of words that are least similar (dissimilar) to a
     * given word.
     * 
     * This method finds the words that are least similar to the provided word using
     * the currently set similarity algorithm.
     * The number of dissimilar words to return is specified by the `numValues`
     * parameter.
     * 
     * @param word      the word for which dissimilar words are to be found
     * @param numValues the number of dissimilar words to retrieve
     * @return an array of strings containing the words that are least similar to
     *         the provided word
     * @throws Exception if an error occurs during the retrieval of dissimilar
     *                   words, such as if the word is not found
     */
    public String[] getDissimilarWords(String word, int numValues) throws Exception {
        return getSimilarWords(word, numValues, false);
    }

    /**
     * Retrieves a specified number of words that are least similar (dissimilar) to
     * a given word.
     * 
     * This method identifies the words that are least similar to the specified word
     * using the currently configured similarity algorithm.
     * The number of dissimilar words to return is determined by the `numValues`
     * parameter.
     * 
     * @param word      the word for which dissimilar words are to be identified
     * @param numValues the number of dissimilar words to retrieve
     * @return an array of strings containing the words that are least similar to
     *         the specified word
     * @throws Exception if an error occurs during the retrieval of dissimilar
     *                   words, such as if the word is not found in the embeddings
     */
    public String[] getDissimilarWords(double[] embedding, int numValues) throws Exception {
        return getSimilarWords(embedding, numValues, false);
    }

    /**
     * Retrieves a specified number of words that are either similar or dissimilar
     * to a given word,
     * based on the specified boolean flag.
     * 
     * This method determines whether to search for similar or dissimilar words
     * using the currently set similarity algorithm.
     * It first locates the word's embedding from the loaded embeddings file and
     * then retrieves the most relevant words
     * based on the specified criteria.
     * 
     * @param word      the word for which similar or dissimilar words are to be
     *                  found
     * @param numValues the number of similar or dissimilar words to retrieve
     * @param similar   a boolean flag indicating whether to search for similar
     *                  words (true) or dissimilar words (false)
     * @return an array of strings containing words that are either similar or
     *         dissimilar to the provided word,
     *         as determined by the `similar` flag and the set similarity algorithm
     * @throws Exception if an error occurs during the retrieval of words, such as
     *                   if the word is not found
     */
    public String[] getSimilarWords(String word, int numValues, boolean similar) throws Exception {
        int wordIndex = getWordIndex(word);
        double[] embedding = this.embeddings[wordIndex];
        return getSimilarWords(embedding, numValues, similar);
    }

    /**
     * Retrieves a specified number of words that are either similar to or
     * dissimilar from a given word vector,
     * based on the specified boolean flag.
     * 
     * This method calculates similarity scores between the provided word vector
     * (`embedding`) and all word vectors
     * loaded from the embeddings file. The similarity is determined using the
     * currently set similarity algorithm.
     * Depending on the `similar` flag, the method returns either the most similar
     * or the most dissimilar words.
     * 
     * The method works as follows:
     * - Computes similarity scores for each word vector in the embeddings file
     * relative to the given `embedding`.
     * - Determines whether to return the most similar or the most dissimilar words
     * based on the `similar` flag.
     * - For Euclidean distance, lower scores indicate higher similarity and higher
     * scores indicate higher dissimilarity.
     * - For other similarity metrics, the relationship between score values and
     * similarity is typically reversed.
     * 
     * @param embedding the vector representation of the word for which similar or
     *                  dissimilar words are to be found
     * @param numValues the number of similar or dissimilar words to retrieve
     * @param similar   a boolean flag indicating whether to find similar words
     *                  (true) or dissimilar words (false)
     * @return an array of strings containing the words that are either most similar
     *         or most dissimilar to the provided
     *         vector, as determined by the `similar` flag and the set similarity
     *         algorithm
     * @throws Exception if an error occurs during similarity calculation or if the
     *                   specified number of similar/dissimilar
     *                   words cannot be retrieved
     */
    public String[] getSimilarWords(double[] embedding, int numValues, boolean similar) throws Exception {
        double[] similarityScores = new double[this.numberOfWords];

        // Initialize an array to hold similarity scores for each word in the embeddings
        // file
        for (int i = 0; i < this.numberOfWords; i++)
            similarityScores[i] = this.Vectorsimilarity.calculate(embedding, this.embeddings[i]);

        // Determine if the similarity metric being used is Euclidean distance
        boolean usingEuclidean = this.Vectorsimilarity == VectorSimilarityMetrics.EUCLIDEAN_DISTANCE
                || this.Vectorsimilarity == VectorSimilarityMetrics.EUCLIDEAN_DISTANCE_NO_SQRT;
        // Decide whether to use minimum or maximum values based on the similarity
        // metric
        // Euclidean distance: lower values indicate higher similarity, higher values
        // indicate higher dissimilarity
        // Other metrics: generally, higher values indicate higher similarity
        boolean useMinimums = usingEuclidean ? similar : !similar;

        // Retrieve indexes of the top 'numValues' words based on similarity scores
        // Use minimum values if Euclidean distance is used and 'similar' is true,
        // or if another metric is used and 'similar' is false
        int[] wordIndexes = useMinimums ? Array.getMinValuesIndexes(similarityScores, numValues)
                : Array.getMaxValuesIndexes(similarityScores, numValues);

        // Initialize arrays to store the most similar or dissimilar words and their
        // corresponding scores
        String[] similarWords = new String[wordIndexes.length];
        double[] similarWordsScores = new double[wordIndexes.length];

        // Populate the arrays with the words and their similarity scores based on the
        // retrieved indexes
        for (int i = 0; i < wordIndexes.length; i++) {
            similarWords[i] = this.words[wordIndexes[i]]; // Get the word corresponding to the index
            similarWordsScores[i] = similarityScores[wordIndexes[i]]; // Get the similarity score for the word
        }

        // Store the results of the most recent similar or dissimilar word search
        // Save the array of similar or dissimilar words for potential future reference
        // or reuse
        this.previousSimilarWords = similarWords;
        // Save the corresponding similarity scores for the similar or dissimilar words
        // This allows for further analysis or display of scores alongside the words
        this.previousSimilarWordsScores = similarWordsScores;

        // Return the array of similar or dissimilar words as the result of the method
        // This provides the caller with the most similar or dissimilar words based on
        // the input parameters
        return similarWords;
    }

    /**
     * Loads all relevant data from the specified words-embeddings file.
     * 
     * This method reads the words-embeddings file to populate the following:
     * - The number of words (`numberOfWords`)
     * - The array of words (`words`)
     * - The word-embedding vectors
     * 
     * It performs the following steps:
     * 1. Counts the number of lines in the file to determine the number of words.
     * 2. Initializes the `words` array to hold the words from the file.
     * 3. Opens a `BufferedReader` to read the file and populate the `words` array
     * and the embeddings vectors.
     * 4. Closes the `BufferedReader` after reading the file to release system
     * resources.
     * 
     * @throws Exception if an error occurs during file reading or data processing.
     */
    public void setWordsAndEmbeddings() throws Exception {
        this.numberOfWords = (int) countFileLines();
        this.words = new String[this.numberOfWords];

        try (BufferedReader buffer = new BufferedReader(new FileReader(this.fileName))) {
            readBufferLinesData(buffer);
            buffer.close();
        }
    }

    /**
     * Counts the number of lines in the words-embeddings file, which corresponds to
     * the number of words in the file.
     * 
     * This method reads the entire file to determine the total number of lines,
     * which is used to ascertain the number of words
     * in the words-embeddings file. It handles potential issues such as very large
     * files by checking if the number of lines exceeds
     * the maximum value for an integer.
     * 
     * The method performs the following steps:
     * 
     * Creates a `Path` object for the file specified by `fileName`.
     * Opens a stream of lines from the file using `Files.lines()` with UTF-8
     * encoding.
     * Counts the number of lines in the file.
     * Checks if the count exceeds the maximum value for an integer, throwing an
     * exception if it does.
     * Returns the number of lines as the result.
     * 
     * @return the number of lines (words) in the words-embeddings file
     * @throws Exception if an error occurs during file reading or if the number of
     *                   lines exceeds the integer limit
     */
    private long countFileLines() throws Exception {
        Path path = Paths.get(this.fileName);

        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            long numberOfWords = stream.count();

            if (numberOfWords > Integer.MAX_VALUE - 1)
                throw new Exception("There are too many words (" + numberOfWords
                        + ") in the provided word-embeddings file: " + getFileName());

            return numberOfWords;
        }
    }

    /**
     * Reads each line from the provided BufferedReader, initializes the relevant
     * data structures, and processes the word embeddings.
     * 
     * This method reads the contents of the words-embeddings file line by line
     * using the provided `BufferedReader`. It initializes
     * the number of features for the embeddings and sets up the data structures to
     * hold the word embeddings. It processes each line
     * to extract the word and its corresponding vector representation. The progress
     * of the loading process is displayed on the console.
     * 
     * The method performs the following steps:
     * 
     * Reads the first line from the `BufferedReader`, which is expected to
     * contain information about the number of vector features.
     * Initializes the `numberOfFeatures` and `embeddings` array based on the
     * number of words and features.
     * Prints a header indicating the start of the file loading process.
     * Iterates over each line in the file to extract and set the word and its
     * vector representation.
     * Updates the progress of the file loading to the console after processing
     * each line.
     * 
     * @param buffer the `BufferedReader` containing the data from the
     *               words-embeddings file
     * @throws Exception if an error occurs during file reading or if the data
     *                   format is incorrect
     */
    private void readBufferLinesData(BufferedReader buffer) throws Exception {
        String embeddingsFileLine = buffer.readLine();

        this.numberOfFeatures = countEmbeddingsFeatures(embeddingsFileLine);
        this.embeddings = new double[this.numberOfWords][getNumberOfFeatures()];

        printFileLoadingHeader();

        for (int i = 0; i < this.numberOfWords; i++) {
            setWordAndEmbeddingsValues(embeddingsFileLine, i);
            ConsoleLoadingMeter.printProgress(i, this.numberOfWords - 1);
            embeddingsFileLine = buffer.readLine();
        }
    }

    /**
     * Splits a string based on common delimiters and stores the delimiter used for
     * future reference.
     * 
     * This method attempts to determine the delimiter used in a given string by
     * checking for the presence
     * of comma-and-space (", "), comma (","), or space (" ") as delimiters. Once
     * the delimiter is detected,
     * the method splits the input string accordingly and stores the detected
     * delimiter for future use.
     * 
     * The method performs the following steps:
     * 
     * Defines an array of possible delimiters (comma-and-space, comma, and
     * space).
     * Iterates over the delimiters to check which one is used in the input
     * string.
     * Splits the input string using the detected delimiter if found.
     * Stores the detected delimiter in an instance variable for future
     * use.
     * Throws an exception if none of the delimiters are found in the input
     * string.
     * 
     * @param delimitedString the string to be split based on the detected delimiter
     * @return an array of substrings obtained by splitting the input string with
     *         the detected delimiter
     * @throws Exception if none of the defined delimiters are found in the input
     *                   string
     */
    private String[] splitWithCommasOrSpaces(String delimitedString) throws Exception {
        // Define possible delimiters
        String[] delimiters = {
                ", ",
                ",",
                " "
        };

        String[] splitString = null;
        String tempDelimiter = null;

        // Try each delimiter to find the correct one
        for (String delimiter : delimiters) {
            splitString = delimitedString.split(delimiter);
            if (splitString.length > 1) {
                tempDelimiter = delimiter;
                break;
            }
        }

        // Throw an exception if no delimiter was found
        if (tempDelimiter == null)
            throw new Exception("Cannot determine the delimiter for the input data.");

        // Store the detected delimiter for future use
        this.delimiter = tempDelimiter;
        return splitString;
    }

    /**
     * Count the number of features in the vector representations of words from a
     * line in the words-embeddings file.
     * 
     * This method calculates the number of features (dimensions) present in the
     * vector representations of words.
     * It does this by processing a line from the words-embeddings file, which
     * typically contains a word followed by its
     * vector representation values. The number of features is determined by
     * counting the number of values in the vector
     * and subtracting one (the word itself).
     * 
     * The method performs the following steps:
     * 
     * Splits the input line using predefined delimiters (comma-and-space,
     * comma, or space).
     * Counts the number of resulting substrings.
     * Subtracts one from the count to exclude the word itself, which is the
     * first element in the split array.
     * 
     * 
     * @param embeddingsLine a line from the words-embeddings file, containing a
     *                       word followed by its vector representation
     * @return the number of features in the vector representations of the words
     * @throws Exception if an error occurs during the delimiter detection or string
     *                   splitting process
     */
    private int countEmbeddingsFeatures(String embeddingsLine) throws Exception {
        // Split the line into parts based on detected delimiters
        String[] stringArray = splitWithCommasOrSpaces(embeddingsLine);
        // The number of features is the total number of values minus one (the word
        // itself)
        int stringLength = stringArray.length;

        return stringLength - 1;
    }

    /**
     * Print information about the loaded words-embeddings file.
     * 
     * This method outputs key details about the words-embeddings file to the
     * console, including:
     * - The path of the file from which embeddings are being loaded.
     * - The delimiter used to separate values in the file.
     * - The total number of words (entries) found in the file.
     * - The number of features (dimensions) for each word vector.
     * 
     * This information helps in verifying the correct loading of data and ensuring
     * that the
     * file contents are parsed as expected. It also provides a quick overview of
     * the dataset.
     */
    private void printFileLoadingHeader() {
        // Print the path of the file being loaded
        System.out.println();
        System.out.println("Loading Embeddings From:\t" + this.fileName);
        // Print the delimiter used in the file
        System.out.println("Delimiter:\t\t\t'" + this.delimiter + "'");
        // Print the total number of words (or lines) found in the file
        System.out.println("#Words:\t\t\t\t" + this.numberOfWords);
        // Print the number of features (dimensions) per word vector
        System.out.println("#Features/Word:\t\t\t" + this.numberOfFeatures);
    }

    /**
     * Set the word and its corresponding vector values from a line in the
     * words-embeddings file.
     * 
     * This method parses a line from the words-embeddings file to extract the word
     * and its vector representation,
     * and then assigns these values to the appropriate arrays.
     * 
     * @param embeddingsFileLine the line from the words-embeddings file containing
     *                           the word and its vector values
     * @param lineNumber         the line number being processed (0-based index)
     * @throws Exception if the line does not contain the expected number of values
     *                   or if it differs from the format of the first line read.
     * 
     *                   The line is expected to have a specific format:
     *                   - The first value is the word itself.
     *                   - The subsequent values are the vector components for that
     *                   word.
     * 
     *                   The method checks if the number of values in the line
     *                   matches the expected count. If not, an exception is thrown,
     *                   indicating that the format of the line does not match the
     *                   expected format.
     */
    private void setWordAndEmbeddingsValues(String embeddingsFileLine, int lineNumber) throws Exception {
        // Split the line into word and vector components using the delimiter
        String[] wordAndEmbeddings = embeddingsFileLine.split(this.delimiter);

        // Check if the number of values in the line matches the expected number of
        // features + 1 (for the word)
        if (wordAndEmbeddings.length != this.numberOfFeatures + 1) {
            // Print a reset console color code and throw an exception if the format is
            // incorrect
            System.out.println(ConsoleColor.RESET + "\n");
            throw new Exception(
                    "Line #" + (lineNumber + 1) + " in " + this.fileName + " has a different format.");
        }

        // Set the word at the current line number
        this.words[lineNumber] = wordAndEmbeddings[0];

        // Parse and set the vector values for the word
        for (int i = 0; i < this.numberOfFeatures; i++)
            this.embeddings[lineNumber][i] = Double.parseDouble(wordAndEmbeddings[i + 1]);
    }

}
