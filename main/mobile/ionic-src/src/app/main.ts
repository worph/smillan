import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';
import { UpgradeModule } from '@angular/upgrade/static';

import { AppModule } from './app.module';

platformBrowserDynamic().bootstrapModule(AppModule).then(platformRef => {
  const upgrade = platformRef.injector.get(UpgradeModule) as UpgradeModule;
  upgrade.bootstrap(document.documentElement,
    [
      'module.location',
      'module.uploads',
      'module.googleapi',
      'module.background.worker',
      'module.properties.service',
      'module.smilanhostservice'
    ]);
});
