# Jenkins Initializing Groovy Scripted Administrative Wizzard (JIGSAW)
## Prerequisities
* Gradle 2.13
* Optional: Idea 2017.1.5

## CLI
* gradle wrapper
* gradle downloadMultipleFiles
* gradle test

## Usage
* to be used from init.groovy.d jenkins directory

## Action Points
* gradle.build
  * setup task dependencies
  * download files will support caching
    * provide md5sum of the file
    * if matches do nothing else re-download

## Issues
* Annotations @WithPlugin and @WithPlugins stop working with Gradle higher than 2.13 and Idea 2017.2.x
  * [Gradle 4.0 directories](https://docs.gradle.org/4.0/release-notes.html#location-of-classes-in-the-build-directory)
  * [Idea 2017.2 no longer sharing output directory with Gradle](https://youtrack.jetbrains.com/issue/IDEA-175172)
  * [Idea and Gradle 4.x output issue](https://github.com/gradle/gradle/issues/2315)
