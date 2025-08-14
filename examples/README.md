# Session Recorder Examples

This directory contains example code demonstrating how to use the Session Recorder library.

## Running the Examples

### Prerequisites

1. Make sure you have the main library built:
   ```bash
   ./gradlew build
   ```

2. Update the API key in `Example.java`:
   ```java
   static class Config {
       public static final String MULTIPLAYER_OTLP_KEY = "your-actual-api-key-here";
   }
   ```

### Building the Examples

```bash
./gradlew :examples:build
```

### Running the Main Example

```bash
./gradlew :examples:run
```

### Running Specific Examples

The `Main.java` file contains multiple example methods:

- `main()` - Basic session recording example
- `continuousSessionExample()` - Continuous session example
- `errorHandlingExample()` - Error handling example

You can modify the `main()` method to call different examples:

```java
public static void main(String[] args) {
    // Run basic example
    // mainExample();
    
    // Run continuous session example
    continuousSessionExample();
    
    // Run error handling example
    // errorHandlingExample();
}
```

## Example Features Demonstrated

- ✅ Session initialization and configuration
- ✅ Resource attributes and session attributes
- ✅ Tags management
- ✅ Plain and continuous sessions
- ✅ Error handling and session cancellation
- ✅ Async operations with CompletableFuture

## Note

These examples are for documentation purposes and are not included in the published library JAR.
