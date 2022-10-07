package software.solid.fluttervlcplayer;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;

public class FlutterVlcPlayerPlugin implements FlutterPlugin, ActivityAware {

    private static final String VIEW_TYPE = "flutter_video_plugin/getVideoView";

    private FlutterVlcPlayerFactory flutterVlcPlayerFactory;

    @SuppressWarnings("deprecation")
    public static void registerWith(io.flutter.plugin.common.PluginRegistry.Registrar registrar) {
        FlutterVlcPlayerFactory flutterVlcPlayerFactory =
                new FlutterVlcPlayerFactory(
                        registrar.messenger(),
                        registrar.textures(),
                        registrar::lookupKeyForAsset,
                        registrar::lookupKeyForAsset
                );
        registrar
                .platformViewRegistry()
                .registerViewFactory(
                        VIEW_TYPE,
                        flutterVlcPlayerFactory
                );
        registrar.addViewDestroyListener(view -> {
            flutterVlcPlayerFactory.stopListening();
            return false;
        });
        flutterVlcPlayerFactory.startListening();
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        final FlutterInjector injector = FlutterInjector.instance();
        flutterVlcPlayerFactory =
                new FlutterVlcPlayerFactory(
                        binding.getBinaryMessenger(),
                        binding.getTextureRegistry(),
                        injector.flutterLoader()::getLookupKeyForAsset,
                        injector.flutterLoader()::getLookupKeyForAsset
                );
        binding
                .getPlatformViewRegistry()
                .registerViewFactory(
                        VIEW_TYPE,
                        flutterVlcPlayerFactory
                );
        flutterVlcPlayerFactory.startListening();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        flutterVlcPlayerFactory.stopListening();
        flutterVlcPlayerFactory = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        flutterVlcPlayerFactory.startListening();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        flutterVlcPlayerFactory.stopListening();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        flutterVlcPlayerFactory.startListening();
    }

    @Override
    public void onDetachedFromActivity() {
        flutterVlcPlayerFactory.stopListening();
    }
}
