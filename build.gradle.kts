plugins {
    kotlin("jvm") version "1.3.21"
    java
}

repositories {
    jcenter()
}

dependencies {
    compile( kotlin("stdlib") )
    compile( "org.apache.commons:commons-csv:1.7")
    compileOnly("org.projectlombok:lombok:1.18.10")
    compile("ch.qos.logback:logback-classic:1.1.7")
    annotationProcessor("org.projectlombok:lombok:1.18.10")
    compile("com.google.guava:guava:28.1-jre")
    testCompile("junit:junit:4.+")
}

tasks.test {
    testLogging {
        events("PASSED", "FAILED", "SKIPPED", "STANDARD_ERROR", "STANDARD_OUT")
    }
}