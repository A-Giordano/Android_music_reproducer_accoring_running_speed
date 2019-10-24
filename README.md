This is a prototype app that uses the Android StepDetector to reproduce, song after song, the one with the closest BPM to the SPM to help the user to have appropriate musing during the running experience.

It requires to have on the phone songs with the actual BPM as the title.

It has been implemented an Application class named RunToBPM in order to be able to access and therefore display and play songs both in MainActivity and in CollectionDysplayer activity. This allows to have cleaner code, avoiding duplications, but above all to not having to pass objects between activities through intents or bundles, resulting in a very cumbersome solution.

The SPM count is performed by a Handler that manages a Thread that reset to 0 the step count every 30 seconds.

