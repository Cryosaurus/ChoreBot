/*
 * Chorebot is a discord bot I made to remind my roommmates and I that we need to take the garbage out
 * Built using the Discord4J library
 * https://github.com/austinv11/Discord4J
 * 
 * Additional credits:
 * Sage for providing the outline of a weekly scheduled task
 * https://stackoverflow.com/questions/20387881/how-to-run-certain-task-every-day-at-a-particular-time-using-scheduledexecutorse
 */

package garbage;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class Chorecore implements IListener<MessageEvent>{

	public static Chorecore INSTANCE; // Singleton instance of the bot.
	private IDiscordClient client; // The instance of the discord client.
	private EventDispatcher dispatcher;
	
	private static final long test1 = 317258850371633152L;
	private static final long test2 = 340920915913211904L;
	private static final long chore = 317154330102726656L;

	public static void main(String[] args) { // Main method
		if (args.length < 1) // Needs a bot token provided
			throw new IllegalArgumentException("This bot needs at least 1 argument!");
		
		INSTANCE = login(args[0]); // Creates the bot instance and logs it in.
		
		resetGarbage(1, 18, 0, 0);
	}

	public Chorecore(IDiscordClient client) {
		this.client = client; // Sets the client instance to the one provided
		this.dispatcher = client.getDispatcher(); // Gets the EventDispatcher instance for this client instance
		this.dispatcher.registerListener(this);
	}
	
	public IDiscordClient getClient(){
		return this.client;
	}

	public static Chorecore login(String token) {
		Chorecore bot = null; // Initializing the bot variable

		ClientBuilder builder = new ClientBuilder(); // Creates a new client builder instance
		builder.withToken(token); // Sets the bot token for the client
		try {
			IDiscordClient client = builder.login(); // Builds the IDiscordClient instance and logs it in
			bot = new Chorecore(client); // Creating the bot instance
		} catch (DiscordException e) { // Error occurred logging in
			System.err.println("Error occurred while logging in!");
			e.printStackTrace();
		}

		return bot;
	}

	public void handle(MessageEvent event) {
		IMessage message = event.getMessage(); // Gets the message from the event object NOTE: This is not the content of the message, but the object itself
		IChannel channel = message.getChannel(); // Gets the channel in which this message was sent.
		
		System.out.println(channel.getLongID());

		//Before parsing commands just check if the bot is responding to itself
		if(event.getAuthor().getName().equals("Chorebot")){
			return;
		}
		
		//Pass along the message to the parser so bot can see if it has to do something
		String reply = Chorecommand.parse(message.getContent());
		if(!reply.isEmpty()){
			System.out.println(reply);
			try {
				// Builds (sends) and new message in the channel that the original message was sent with the content of the original message.
				new MessageBuilder(this.client).withChannel(channel).withContent(reply).build();
			} catch (RateLimitException e) { // RateLimitException thrown. The bot is sending messages too quickly!
				System.err.print("Sending messages too quickly!");
				e.printStackTrace();
			} catch (DiscordException e) { // DiscordException thrown. Many possibilities. Use getErrorMessage() to see what went wrong.
				System.err.print(e.getErrorMessage()); // Print the error message sent by Discord
				e.printStackTrace();
			} catch (MissingPermissionsException e) { // MissingPermissionsException thrown. The bot doesn't have permission to send the message!
				System.err.print("Missing permissions for channel!");
				e.printStackTrace();
			}
		}
	}

	public static void resetGarbage(int dayInterval, int hour, int min, int sec){
		
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.of("America/Los_Angeles");
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNext;
		
//		zonedNext = zonedNow.withHour(hour).withMinute(min).withSecond(sec);
		zonedNext = zonedNow.plusHours(hour).plusMinutes(min).plusSeconds(sec); //test line
//		if((zonedNow.compareTo(zonedNext) > 0) && (dayInterval > 0)){
//			zonedNext = zonedNext.plusDays(dayInterval);
//		}
		
		Duration duration = Duration.between(zonedNow, zonedNext);
		long initialDelay = duration.getSeconds();
		System.out.println("Initial delay of: " + initialDelay);
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);            
//		scheduler.scheduleAtFixedRate(new Chorereminder(), initialDelay, 24*60*60, TimeUnit.SECONDS);
		scheduler.scheduleAtFixedRate(new Chorereminder(), 60, 60, TimeUnit.SECONDS); //test line
	}
	
	public static void reminder(String message){
		//Workaround for the static problem
		IDiscordClient tempClient = INSTANCE.getClient();
		try {
			// Builds (sends) and new message in the channel that the original message was sent with the content of the original message.
			new MessageBuilder(tempClient).withChannel(test1).withContent(message).build();
		} catch (RateLimitException e) { // RateLimitException thrown. The bot is sending messages too quickly!
			System.err.print("Sending messages too quickly!");
			e.printStackTrace();
		} catch (DiscordException e) { // DiscordException thrown. Many possibilities. Use getErrorMessage() to see what went wrong.
			System.err.print(e.getErrorMessage()); // Print the error message sent by Discord
			e.printStackTrace();
		} catch (MissingPermissionsException e) { // MissingPermissionsException thrown. The bot doesn't have permission to send the message!
			System.err.print("Missing permissions for channel!");
			e.printStackTrace();
		}
	}
}
