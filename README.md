# TAO Simple REST Shell

This is a simple REST shell based on the Spring HATEOAS-compliant REST shell ([https://github.com/spring-projects/rest-shell/](https://github.com/spring-projects/rest-shell/)).
Therefore, the below sections are inspired from the aforementioned project sections.

# Getting Started

This project is a command-line shell that aims to make writing REST-based applications easier. It is based on [spring-shell](http://github.com/springsource/spring-shell) and integrated with [Spring HATEOAS](https://github.com/springsource/spring-hateoas) in such a way that REST resources that output JSON compliant with Spring HATEOAS can be discovered by the shell and interactions with the REST resources become much easier than by manipulating the URLs in bash using a tool like `curl`.

The rest-shell provides a number of useful commands for discovering and interacting with REST resources. For example `discover` will discover what resources are available and print out an easily-readable table of rels and URIs that relate to those resources. Once these resources have been discovered, the `rel` of those URIs can be used in place of the URI itself in most operations, thus cutting down on the amount of typing needed to issue HTTP requests to your REST resources.

_NOTE: If you want tab completion of discovered rels, just use the `--rel` flag._

### SSL Certificate Validation

If you generate a self-signed certificate for your server, by default the rest-shell will complain and refuse to connect. This is the default behavior of RestTemplate. To turn off certificate and hostname checking, use the `ssl validate --enabled false` command.

### HTTP Basic authentication

There is also a convenience command for setting an HTTP Basic authentication header. Use `auth basic --username user --password passwd` to set a username and password to base64 encode and place into the Authorization header that will be part of the current session's headers.

You can clear the authentication by using the `auth clear` command or by removing the Authorization header using the `headers clear` command.

### Commands

The tao-shell provides the following rest-shell commands:

* `discover` - Find out what resources are available at the given URI. If no URI is given, use the baseUri.
* `follow` - Set the baseUri to the URI assigned to this given `rel` but do not discover resources.
* `list` - Discover the resources available at the current baseUri.
* `baseUri` - Set the base URI used for this point forward in the session. Relative URIs will be calculated relative to this setting.
* `headers set` - Set an HTTP header by passing this command a `--name` and `--value` parameter.
* `headers clear` - Clear all HTTP headers set in this session.
* `headers list` - Print out the currently-set HTTP headers for this session.
* `history list` - List the URIs previously set as baseUris during this session.
* `history go` - Jump to a URI by pulling one from the history.
* `var clear` - Clear this shell's variable context.
* `var get` - Get a variable in this shell's context.
* `var list` - List variables currently set in this shell's context.
* `var set` - Set a variable in this shell's context.
* `up` - Traverse one level up in the URL hierarchy.
* `get` - HTTP GET from the given path.
* `post` - HTTP POST to the given path, passing JSON given in the `--data` parameter.
* `put` - HTTP PUT to the given path, passing JSON given in the `--data` parameter.
* `delete` - HTTP DELETE to the given path.
* `auth basic` - Set an HTTP Basic authentication token for use in this session.
* `auth clear` - Clear the Authorization header currently in use.
* `ssl validate` - Disable certificate checking to work with self-signed certificates.

### Setting the proper TAO context

Before issuing commands against TAO services, you have to prepare the proper HTTP context.
By default, the shell is started having as base URI:

        http://localhost:8080>

Let's change it:

        http://localhost:8080>baseUri https://localhost:8443

Second, let's ensure self-signed SSL certificate is not rejected:

        https://localhost:8443>ssl validate --enabled false

And the last thing, let's pass in the Basic Authentication credentials for the new base URI:

        https://localhost:8443>auth basic --username admim --password admin

### Simple GET requests

To call simple REST endpoints, without any query parameter, just type in the relative path of the endpoint:

        https://localhost:8443>get topology/

### Passing query parameters

If you're calling URLs that require query parameters, you'll need to pass those as a JSON-like fragment in the `--params` parameter to the `get` and `list` commands. Here's an example of calling a URL that expects parameter input:

		https://localhost:8443:> get component/list --params "{id: 'RigidTransformResample'}"

### Outputing results to a file

It's not always desirable to output the results of an HTTP request to the screen. It's handy for debugging but sometimes you want to save the results of a request because they're not easily reproducible or any number of other equally valid reasons. All the HTTP commands take an `--output` parameter that writes the results of an HTTP operation to the given file. For example, to output the above search to a file:

		https://localhost:8443:> get component/list --params "{id: 'RigidTransformResample'}" --output output.txt
		>> output.txt
		https://localhost:8443:>

### Sending complex JSON

Because the `tao-shell` uses the [spring-shell](http://github.com/springsource/spring-shell) underneath, there are limitations on the format of the JSON data you can enter directly into the command line. If your JSON is too complex for the simplistic limitations of the shell `--data` parameter, you can simply load the JSON from a file or from all the files in a directory.

When doing a `post` or `put`, you can optionally pass the `--from` parameter. The value of this parameter should either be a file or a directory. If the value is a directory, the shell will read each file that ends with `.json` and make a POST or PUT with the contents of that file. If the parameter is a file, then the `rest-shell` will simpy load that file and POST/PUT that data in that individual file.

### Shelling out to bash

One of the nice things about spring-shell is that you can directly shell out commands to the underlying terminal shell. This is useful for doing things like load a JSON file in an editor. For instance, assume I have the Nano editor command `nano` in my path. I can then load a JSON file for editing from the tao-shell like this:

		https://localhost:8443:> ! nano my_component.json
		https://localhost:8443:>

I then edit the file as I wish. When I'm ready to POST that data to the server, I can do so using the `--from` parameter:

		https://localhost:8443:> post component/import --from my_component.json
		1 items uploaded to the server using POST.
		https://localhost:8443:>

### Setting context variables

You can also work with context variables during your shell session. This is useful for saving settings you might reference often. The tao-shell now integrates Spring Expression Language support, so these context variables are usable in expressions within the shell.

##### Working with variables

		https://localhost:8443:> var set --name specialUri --value https://localhost:8443/docker/
		https://localhost:8443:> var get --name specialUri
		https://localhost:8443/docker/
		https://localhost:8443:> var list
		{
			"responseHeaders" : {
				... HTTP headers from last request
			},
			"responseBody" : {
				... Body from the last request
			},
			"specialUri" : "https://localhost:8443/docker/",
			"requestUrl" : ... URL from the last request,
			"env" : {
				... System properties and environment variables
			}
		}

The variables are accessible from SpEL expressions which are valid in a number of different contexts, most importantly in the `path` argument to the HTTP and discover commands, and in the `data` argument to the `put` and `post` commands.
