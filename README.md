# Udacity-Capstone-Project

## Reddit roulette

An App that lets you download subreddits and pick random posts from each of them.
Supposedly, there are several heuristics for picking the posts, 
but initially they're picking just using a uniform random distribution.

It features:

 * As 03/2020 (Target API 29), the latest-version dependencies, among which:
 ..* the use of ViewBinding in place of Butterknife
 ..* Okhttp for all network requests
 ..* The use of JUnit5 for running tests
 * A simple widget which lets you choose
 * Choose between Dark and light themes
 * Use of Firebase Analytics (Metrics, Crashlytics, and Performance) (Note: the google-services.json is not included in the repo)
 * Native handling of the [Reddit API](https://www.reddit.com/dev/api/)*.
 * Read posts offline
 * Free (with-Ads) and paid (Ads-free) tiers as build flavors 
 * Testing with [Espresso](https://developer.android.com/training/testing/espresso), [Mockito](https://site.mockito.org/), and [Robolectric](http://robolectric.org/).

(*) yes, there are wrappers around the internet (as e.g. [JRAW](https://github.com/mattbdean/JRAW)), but I didn't need all the stuff, 
and moreoever it was the perfect use for showcasing OkHTTP features.