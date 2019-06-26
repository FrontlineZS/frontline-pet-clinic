package zs.frontline.frontlinepetclinicweb;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;

@SpringBootApplication
public class FrontlinePetClinicApplication {
    private static UnaryOperator<Object> IDENTITY_FN = (t) -> t;
    
    @SuppressWarnings("unchecked")
    public static <T> UnaryOperator<T> identityFunction() {
        return (UnaryOperator<T>) IDENTITY_FN;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(FrontlinePetClinicApplication.class, args);
        
        Set<Integer> union = union(
            new HashSet<>(Arrays.asList(1, 2, 3)),
            new HashSet<>(Collections.singleton(5))
        );
        
        System.out.println(union);
        
        String[] strings = {"juta", "konopie", "nylon"};
        UnaryOperator<String> sameString = identityFunction();
        for (String s : strings) {
            System.out.println(sameString.apply(s));
        }
        
        Number[] numbers = {1, 2.0, 3L};
        UnaryOperator<Number> sameNumber = identityFunction();
        for (Number n : numbers) {
            System.out.println(sameNumber.apply(n));
        }
        
        
        // MOCK
        List<Book> mockBookList = Arrays.asList(
            new Book(19.99, "Czysty kod", "twarda"),
            new Book(29.99, "Pani jeziora", "miękka"),
            new Book(39.99, "Hobbit", "twarda")
        );
        
        
        // CONSUMER INTERFACE
        Consumer<Book> printTitle = book -> System.out.println(book.title);
        Consumer<Book> printCover = book -> System.out.println(book.cover + "\n-----------------");
        mockBookList.forEach(printTitle.andThen(printCover));
        
        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5);
        Consumer<Integer> consumer1 = FrontlinePetClinicApplication::multipleTwice;
        Consumer<Integer> consumer2 = FrontlinePetClinicApplication::multipleThrice;
        ints.forEach(consumer1.andThen(consumer2));
        
        
        // FUNCTION INTERFACE
        Function<Book, String> stringPriceFunction = book -> Double.toString(book.price);
        System.out.println(stringPriceFunction.apply(mockBookList.get(0)) + "\n");
        System.out.println("-----------------");
        
        
        // .compose
        Function<String, String> concatenateString = s -> "Cena " + s;
        mockBookList.forEach(book -> System.out.println(
            concatenateString
                .compose(stringPriceFunction)
                .apply(book))
        );
        System.out.println("-----------------");
        
        
        // .andThen
        mockBookList.forEach(book -> System.out.println(
            stringPriceFunction
                .andThen(concatenateString)
                .apply(book)
        ));
        System.out.println("-----------------");
        
        
        // .identity
        Function<Book, Book> returnBookIdentity = Function.identity();
        System.out.println(returnBookIdentity.apply(mockBookList.get(0)));
        System.out.println("-----------------");
        
        
        // PREDICATE INTERFACE
        Predicate<String> predicate = a -> a.substring(1).isEmpty();
        System.out.println(predicate.test("a")); // true
        System.out.println(predicate.test("ab")); // false
        System.out.println("-----------------");
        
        
        // UNARY OPERATOR INTERFACE
        UnaryOperator<Book> makeCopy = Book::clone;
        
        Book book1 = mockBookList.get(0);
        Book unaryProcessedBook = makeCopy.apply(book1);
        System.out.println(book1);
        System.out.println(unaryProcessedBook);
        System.out.println("Are these books equals ?");
        System.out.println(unaryProcessedBook.equals(book1));
        System.out.println("-----------------");
        
        
        Map<String, Integer> nameMap = new HashMap<String, Integer>() {
            {
                put("John", 1);
                put("Robert", 2);
                put("Patric", 3);
            }
        };
        nameMap.computeIfAbsent("testName", name -> nameMap.put(name, 4));
        
        
        Function<Integer, String> intToString = Object::toString;
        Function<String, String> makeQuote = string -> "'" + string + "'";
        System.out.println("Quoted integer " + makeQuote
            .compose(intToString)
            .apply(5)
        );
        System.out.println("-----------------");
        
        
        Supplier<Double> getValue = () -> 3d;
        Function<Supplier<Double>, String> supplierStringFunction = supplier -> String.valueOf(supplier.get());
        Function<String, String> makeQuotedDouble = s -> "'" + s + "'";
        System.out.println("Quoted double supplier " + makeQuotedDouble
            .compose(supplierStringFunction)
            .apply(getValue)
        );
        System.out.println("-----------------");
        
        
        Favorites favorites = new Favorites();
        favorites.putFavorite(String.class, "Java");
        favorites.putFavorite(Integer.class, 0xcafababe);
        favorites.putFavorite(Class.class, Favorites.class);
        
        String favoriteString = favorites.getFavorite(String.class);
        int favoriteInteger = favorites.getFavorite(Integer.class);
        Class<?> favoriteClass = favorites.getFavorite(Class.class);
        
        System.out.printf(
            "%s %x %s%n",
            favoriteString,
            favoriteInteger,
            favoriteClass.getName()
        );
        System.out.println("-----------------");
        
        // GENERICS
        Chooser<String> chooser = new Chooser<>(Arrays.asList("123", "456", "789"));
        System.out.println(chooser.choose());
        System.out.println("-----------------");
        
        Stack<String> stacks = new Stack<>();
        for (String arg : args) {
            stacks.push(arg);
        }
        
        while (!stacks.isEmpty()) {
            System.out.println(stacks.pop().toUpperCase());
        }
    }
    
    private static class Stack<E> {
        private E[] elements;
//        private Object[] elements;
        private int size = 0;
        private static final int DEFAULT_INITIAL_CAPACITY = 4;
        
        @SuppressWarnings("unchecked")
        private Stack() {
            elements = (E[]) new Object[DEFAULT_INITIAL_CAPACITY];
//            elements = new Object[DEFAULT_INITIAL_CAPACITY];
        }
        
        private void push(E el) {
            ensureCapacity();
            elements[size++] = el;
        }
    
        private E pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            
//            @SuppressWarnings("unchecked") // pop needs elements parametrized with E so casting is safely
//            E result = (E) elements[--size];
            E result = elements[--size];
            elements[size] = null;
            
            return result;
        }
    
        private boolean isEmpty() {
            return size == 0;
        }
    
        private void ensureCapacity() {
            if (elements.length == size) {
                elements = Arrays.copyOf(elements, 2 * size + 1);
            }
        }
    }
    
    private static class Chooser<E> {
        private final List<E> choiceList;
    
        private Chooser(Collection<E> choices) {
            this.choiceList = new ArrayList<>(choices);
        }
    
        private E choose() {
            Random rnd = ThreadLocalRandom.current();
            return choiceList.get(rnd.nextInt(choiceList.size()));
        }
    }
    
    static Annotation getAnnotation(AnnotatedElement element, String annotationTypeName) {
        Class<?> annotationType = null;
        
        try {
            annotationType = Class.forName(annotationTypeName);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
        
        return element.getAnnotation(annotationType.asSubclass(Annotation.class));
    }
    
    private static class Favorites {
        private Map<Class<?>, Object> favorites = new
            HashMap<>();
    
        private <T> void putFavorite(Class<T> type, T instance) {
            favorites.put(
                Objects.requireNonNull(type),
                type.cast(instance)
            );
        }
    
        private <T> T getFavorite(Class<T> type) {
            return type.cast(favorites.get(type));
        }
    }
    
    @Data
    @AllArgsConstructor
    private static class Book implements Cloneable {
        private double price;
        private String title;
        private String cover;
        
        @Override
        public Book clone() {
            try {
                return (Book) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
    
    public static <E> Set<E> union(Set<E> s1, Set<E> s2) {
        Set<E> result = new HashSet<>(s1);
        result.addAll(s2);
        return result;
    }
    
    public static void multipleTwice(int a) {
        System.out.println(a * 2);
    }
    
    public static void multipleThrice(int a) {
        System.out.println(a * 3 + "\n");
    }
}