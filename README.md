# shop-demo

## Idea

The main ideas was to implement the solution in DDD / rich model way, instead of a CRUD / anemic model one.

## How to run

    mvn spring-boot:run
    
Maven being installed is a prerequisite.

## API documentation

http://localhost:8080/swagger-ui.html

You can navigate between Products and Orders in the top right corner dropdown.

## Answers

### Authentication protocol / method propose

I'd go for JSON Web Token or... cookies.

Cookies because it's good, old technology. Old in a good way - well understood and with mature implementation pretty much everywhere.

JWT because not every client can use cookies, e.x. mobile devices. It's a popular standard, and a well implemented one.  

### How can you make the service redundant? What considerations should you do?

For the sake of this writing, I'll consider "redundant" as a "failsafe" and consider it's high availability.

In this case, the key conept is to get rid of the state from the application. It makes clustering easier.

I'd go for a load balancing and clustering. We need to consider state synchronisation or - even better - moving the state out of the application - to a remote database or a distributed cache. Moving the state out of the application is profitable because all the data synchronisation work is done by external data sources.

Important thing to consider is we don't have use a distributed session in this case. A proper handling of 401 status on the client side + a sticky session should do the trick. As a next step, we could think of consistent hashing - a one based on a unique identifier of a client.