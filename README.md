# jawbone
Jawbone is a Java WordNet API library (hence the "Jaw" portion of the name - an acronym for Java API for WordNet). It makes it very easy to search the Wordnet data files for terms, either all terms or just those terms matching some search criteria. It is compatible with WordNet versions 2.1 and 3.0.

The library knows how to parse the data files (data.\*) and index files (index.\*). These two sets of files constitute the majority of data in WordNet. The library does not parse the exception files (\*.exc) or the verb files (\*.vrb).

The JawboneDemo class (included in the source) gives an example of how to initialize and use the library. Here's a brief sample of that code:

```
  // Set the path to the data files
  Dictionary.initialize("C:/Program Files/WordNet/2.1/dict");
  
  // Get an instance of the Dictionary object
  Dictionary dict = Dictionary.getInstance();
  
  // Declare a filter for all terms starting with "car", ignoring case
  TermFilter filter = new StartsWithFilter("car", true);
  
  // Get an iterator to the list of nouns
  Iterator iter = 
    dict.getIndexTermIterator(PartOfSpeech.NOUN, 1, filter);
```

The Dictionary class is initialized with the location of WordNet's data files (under Windows, the files are usually in C:\Program Files\WordNet\2.1\dict). Once the Dictionary factory (getInstance()) is used to get a reference to a Dictionary object, the following methods are available in order to get an iterator to an array of IndexTerm objects:

```
  public Iterator getIndexTermIterator(final PartOfSpeech pos,
                                                  final int maxLimit,
                                                  final TermFilter filter)

  public Iterator getIndexTermIterator(final int maxLimit,
                                                  final TermFilter filter)
```

The first method returns an iterator for the specified part of speech (e.g., PartOfSpeech.ADVERB). The maxLimit argument is the maximum number of search results to return (-1 for no limit). If you want to apply a filter for the search terms, pass a TermFilter instance, or null if you don't want a filter applied.

The second method returns an iterator for all parts of speech (noun, adverb, adjective and verb).

TermFilter is an interface that defines one method: accept(String). It is used internally during searches. The available filters to use are:

* ContainsFilter - Matches terms that contain the argument passed in the constructor: ContainsFilter(String word, boolean ignoreCase)
* EndsWithFilter - Matches terms that end with the argument passed in the constructor: EndsWithFilter(String word, boolean ignoreCase)
* ExactMatchFilter - Matches terms that are exactly the same as the argument passed in the constructor: ExactMatchFilter(String word, boolean ignoreCase)
* RegexFilter - Matches terms that match the regular expression string passed in the constructor: RegexFilter(String regex, boolean ignoreCase)
* SimilarFilter - Matches terms that are similar to the term passed in the constructor, with a maximum distance also specified in the constructor: SimilarFilter(String word, boolean ignoreCase, int maxDistance) (this class uses the Levenshtein algorithm to compute the distance)
* SoundFilter - Matches terms that sound like the term passed in the constructor: SoundFilter(String word, boolean ignoreCase)
* StartsWithFilter - Matches terms that start with the argument passed in the constructor: StartsWithFilter(String word, boolean ignoreCase)
* WildcardFilter - Matches terms that match the wildcard pattern passed in the constructor: WildcardFilter(String word, boolean ignoreCase) (this class uses the Wildcard code; see that page for more info)

All the comparisons used in the filters listed above are done with the IndexTerm object's lemma.

Update: Curutari has graciously added some helpful methods:

* Synset method: long get9DigitID() - Get the 9-digit unique synset identifier by adding the 1-digit POS numerical prefix to the beginning of the 8-digit offset of the synset
* Synset method: List getRelatedSynsets(String) - Get the synsets of a particular type (e.g., hypernyms)
* Dictionary method: Synset getSynset(long, char) - Get a synset using its 8 digit file offset and part of speech character (e.g., 'n')
* Dictionary method: Synset getSynset(long, PartOfSpeech) - Get a synset using its 8 digit file offset and part of speech
* Dictionary method: Synset getSynset(long) - Get a synset using its 9-digit synset ID
* PartOfSpeech prefix field - Added support for a numerical prefix for the parts of speech (e.g., 1 for nouns)

Thanks, Curutari!

An Ant build.xml file is included for building the application. The file includes the following targets of interest:

* dist - Generate the jawbone.jar file
* javadoc - Generate the Javadocs in the docs subdirectory

The source code is released under the MIT license.
