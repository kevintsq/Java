package lab.lab5.src;

class PathNotFoundException extends Exception {
    PathNotFoundException(final Path path) {
        if (path != null) {
            System.err.println(path + " Not Found!");
        } else {
            System.err.println("NullPointer");
        }
    }
}